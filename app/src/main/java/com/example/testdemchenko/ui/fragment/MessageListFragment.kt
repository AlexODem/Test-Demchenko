package com.example.testdemchenko.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sparktestdemchenko.R
import com.example.sparktestdemchenko.databinding.FragmentMessageListBinding
import com.example.testdemchenko.getMessageViewModel
import com.example.testdemchenko.ui.adapter.MessageListAdapter
import com.example.testdemchenko.ui.model.MessageAction
import com.example.testdemchenko.ui.model.UIMessage
import com.example.testdemchenko.ui.util.*
import com.example.testdemchenko.ui.viewmodel.MessagesViewModel


class MessageListFragment : Fragment() {

    private lateinit var viewModel: MessagesViewModel

    private var adapter: MessageListAdapter? = null
    private val linearLayoutManager by lazy { LinearLayoutManager(requireContext()) }

    private var binding: FragmentMessageListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentMessageListBinding.inflate(inflater, container, false)

            viewModel = getMessageViewModel(requireContext().applicationContext, this)

            adapter = MessageListAdapter(
                onClickListener = { message ->
                    navigationToDetailsFragment(resources.screenSize)
                    setFragmentState(message)
                }, onLongClickListener = { message, action ->
                    updateMessages(message, action)
                })

            setFragmentResultListener(MESSAGE_UPDATES_REQUEST_KEY) { _, bundle ->
                val message = bundle.getSerializable(MESSAGE_KEY) as UIMessage
                val action = bundle.getSerializable(ACTION_KEY) as MessageAction
                updateMessages(message, action)
            }

            binding?.rvMessageList?.layoutManager = linearLayoutManager
            binding?.rvMessageList?.adapter = adapter

            if (isNetworkConnected(requireContext())) {
                viewModel.chekUpdateAndLoadList()
            } else {
                viewModel.getMessageList()
            }

            observeMessages()
            observeLoadingIndicator()
            observeNewMessage()

            adapter?.let { adapter ->
                binding?.rvMessageList?.addOnScrollListener(
                    onScrollListener(
                        adapter,
                        linearLayoutManager
                    )
                )
            }
        }
        return binding?.root
    }

    private fun navigationToDetailsFragment(screenSize: Int) {
        if (screenSize <= Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            findNavController().navigate(R.id.action_MessageListFragment_to_MessageDetailsFragment)
        }
    }

    private fun observeNewMessage() {
        viewModel.newMessage.observe(requireActivity(), { newMessage ->
            adapter?.addNewMessage(newMessage)
            binding?.rvMessageList?.scrollToPosition(0)
        })
    }

    private fun updateMessages(message: UIMessage, action: MessageAction) {
        when (action) {
            MessageAction.READ -> adapter?.updateMessages(message)
            MessageAction.DELETE -> adapter?.deleteMessages(message)
        }
        if (isNetworkConnected(requireContext()).not())
            message.wasChangedOffline = true
        viewModel.updateMessage(message)
    }

    private fun observeLoadingIndicator() {
        viewModel.isLoading.observe(requireActivity(), { isLoading ->
            binding?.progressBar?.isVisible = isLoading
            binding?.rvMessageList?.isVisible = isLoading.not()
        })

        viewModel.isPageLoading.observe(requireActivity(), { isLoading ->
            if (isLoading) {
                adapter?.showProgress()
            } else {
                adapter?.hideProgress()
            }
        })
    }

    private fun observeMessages() {
        viewModel.messages.observe(requireActivity(), { messages ->
            if (adapter?.itemCount == 0 && messages.isNullOrEmpty().not()) {
                setFragmentState(messages[0])
            }
            adapter?.addMessages(messages)
        })
    }

    private fun setFragmentState(message: UIMessage) {
        setFragmentResult(MESSAGE_DETAILS_REQUEST_KEY, bundleOf(MESSAGE_KEY to message))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun onScrollListener(
        adapter: MessageListAdapter,
        linearLayoutManager: LinearLayoutManager
    ) = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkCurrentScrollPosition(adapter, linearLayoutManager)
        }
    }

    private fun checkCurrentScrollPosition(
        adapter: MessageListAdapter,
        layoutManager: LinearLayoutManager
    ) {
        if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
            onScrolledToBottom()
        }
    }

    private fun onScrolledToBottom() {
        adapter?.let { adapter ->
            if (isNetworkConnected(requireContext())) {
                if (adapter.isLoading().not()) {
                    viewModel.loadNextPage()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_email) {
            if (isNetworkConnected(requireContext())) {
                viewModel.addNewMessage()
            } else {
                showErrorMessage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showErrorMessage() {
        val info = getString(R.string.string_message_id)
        Toast.makeText(
            context,
            Html.fromHtml("<font color='#F44336'>$info</font>"),
            Toast.LENGTH_SHORT
        ).show()
    }

}

