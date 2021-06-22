package com.example.testdemchenko.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.sparktestdemchenko.R
import com.example.sparktestdemchenko.databinding.FragmentMessageDetailsBinding
import com.example.testdemchenko.ui.LaunchActivity
import com.example.testdemchenko.ui.model.MessageAction
import com.example.testdemchenko.ui.model.UIMessage
import com.example.testdemchenko.ui.util.*


class MessageDetailsFragment : Fragment() {
    private var binding: FragmentMessageDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMessageDetailsBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        setFragmentResultListener(MESSAGE_DETAILS_REQUEST_KEY) { _, bundle ->
            val message = bundle.getSerializable(MESSAGE_KEY) as UIMessage

            binding?.let { binding ->
                binding.progressBar.visibility = View.GONE

                if (resources.screenSize <= Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                    (activity as LaunchActivity).setToolbarTitle("")
                    binding.toolbarDivider.visibility = View.GONE
                } else {
                    binding.divider.visibility = View.GONE
                }

                binding.tvSubject.text = message.subject
                binding.previewBlock.tvFrom.text = message.from
                binding.previewBlock.tvPreview.text = message.preview
                binding.previewBlock.tvDate.text = DateFormat.printFullDate(message.date)

                if (message.read.not()) {
                    message.read = true
                    updateMessage(message, MessageAction.READ)
                }

                setButtonReadText(message)

                binding.btRead.setOnClickListener {
                    message.read = !message.read
                    setButtonReadText(message)
                    updateMessage(message, MessageAction.READ)
                }

                binding.btDelete.setOnClickListener {
                    activity?.onBackPressed()
                    message.deleted = true
                    updateMessage(message, MessageAction.DELETE)
                }
            }

        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_add_email).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


    private fun updateMessage(message: UIMessage, action: MessageAction) {
        setFragmentResult(MESSAGE_UPDATES_REQUEST_KEY, bundleOf(MESSAGE_KEY to message, ACTION_KEY to action))
    }

    private fun setButtonReadText(message: UIMessage) {
        val textRes = if (message.read) R.string.unread else R.string.read
        binding?.btRead?.setText(textRes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}