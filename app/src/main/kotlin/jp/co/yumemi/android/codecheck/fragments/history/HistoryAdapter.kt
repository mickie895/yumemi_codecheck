package jp.co.yumemi.android.codecheck.fragments.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.history.SearchHistory
import jp.co.yumemi.android.codecheck.databinding.LayoutHistoryItemBinding
import java.text.DateFormat

private val diff_util = object : DiffUtil.ItemCallback<SearchHistory>() {
    override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
        return oldItem == newItem
    }
}

/**
 * 履歴表示用のアダプタ
 */
class HistoryAdapter(private val onHistoryClickedListener: OnHistoryClickedListener) : ListAdapter<SearchHistory, HistoryAdapter.HistoryViewHolder>(diff_util) {
    /**
     * 履歴のアイテムをクリックしたことが通知されるリスナ
     */
    interface OnHistoryClickedListener {
        fun onHistoryClicked(query: String)
    }

    class HistoryViewHolder(val binding: LayoutHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCurrentContext(): Context {
            return binding.root.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = LayoutHistoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        with(getItem(position)) {
            holder.binding.historyQuery.text = this.query
            holder.binding.historyDate.text = holder.getCurrentContext().getString(
                R.string.history_date_format,
                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
                    .format(this.searchedDate),
            )
            holder.itemView.setOnClickListener {
                onHistoryClickedListener.onHistoryClicked(this.query)
            }
        }
    }
}
