package com.example.sparktestdemchenko.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.sparktestdemchenko.R
import com.example.sparktestdemchenko.databinding.ItemMessageBinding
import com.example.sparktestdemchenko.databinding.ItemProgressBinding
import com.example.sparktestdemchenko.ui.model.MessageAction
import com.example.sparktestdemchenko.ui.model.UIMessage
import com.example.sparktestdemchenko.ui.util.DateFormat

class MessageListAdapter(
    private val onClickListener: (message: UIMessage) -> Unit,
    private val onLongClickListener: (message: UIMessage, action: MessageAction) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList = mutableListOf<UIMessage?>()

    fun addMessages(messages: List<UIMessage>) {
        dataList.addAll(messages)
        notifyDataSetChanged()
    }

    fun addNewMessage(message: UIMessage) {
        dataList.add(0, message)
        notifyItemInserted(0)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            ViewHolder(
                ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ProgressHolder(
                ItemProgressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ITEM -> {
                if (holder is ViewHolder) {
                    val message = dataList[position]
                    message?.let {
                        holder.bind(message,
                            { onClickListener(it) },
                            { message, action -> onLongClickListener(message, action) })
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position] == null)
            TYPE_PROGRESS
        else
            TYPE_ITEM
    }

    fun updateMessages(message: UIMessage) {
        val position = dataList.indexOf(message)
        dataList[position] = message
        notifyItemChanged(position)
    }

    fun deleteMessages(message: UIMessage) {
        if (message.deleted) {
            val position = dataList.indexOf(message)
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun showProgress() {
        dataList.add(null)
        notifyItemInserted(dataList.size)
    }

    fun isLoading(): Boolean {
        return dataList.contains(null)
    }

    fun hideProgress() {
        dataList.remove(null)
        notifyItemRemoved(dataList.size)
    }

    class ViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            message: UIMessage,
            onClickListener: (message: UIMessage) -> Unit,
            updateMessages: (message: UIMessage, action: MessageAction) -> Unit
        ) {
            binding.ivTitle.text = message.from
            binding.tvSubject.text = message.subject
            binding.tvPreview.text = message.preview
            binding.tvDate.text = DateFormat.printShortDate(message.date)
            binding.root.setOnClickListener { onClickListener(message) }
            binding.root.setOnLongClickListener {
                showPopUpMenu(message, updateMessages)
                return@setOnLongClickListener true
            }

            binding.vReadIndicator.visibility = if (message.read) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }

        private fun showPopUpMenu(
            message: UIMessage,
            updateMessages: (message: UIMessage, action: MessageAction) -> Unit
        ) {
            val popupMenu = PopupMenu(binding.root.context, binding.tvDate)
            popupMenu.inflate(if (message.read) R.menu.popupmenu_unread else R.menu.popupmenu_read)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.un_read, R.id.read -> {
                        message.read = !message.read
                        updateMessages(message, MessageAction.READ)
                        true
                    }
                    R.id.delete -> {
                        message.deleted = !message.deleted
                        updateMessages(message, MessageAction.DELETE)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    class ProgressHolder(binding: ItemProgressBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_PROGRESS = 2
    }

}