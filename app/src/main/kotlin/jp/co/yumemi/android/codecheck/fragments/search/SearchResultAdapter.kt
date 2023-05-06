/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.fragments.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.LayoutItemBinding
import jp.co.yumemi.android.codecheck.viewmodels.SearchResultItem

private val searchDiffUtil = object : DiffUtil.ItemCallback<SearchResultItem>() {
    override fun areItemsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
        return oldItem.isSameItem(newItem)
    }

    override fun areContentsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
        return oldItem == newItem
    }
}

class SearchResultAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<SearchResultItem, SearchResultAdapter.SearchResultViewHolder>(searchDiffUtil) {

    class SearchResultViewHolder(val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun getCurrentContext(): Context {
            return binding.root.context
        }
    }

    interface OnItemClickListener {
        fun itemClick(item: SearchResultItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = LayoutItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = getItem(position)

        // 項目によって表示する内容を変更する
        holder.binding.itemName.text = when (item) {
            SearchResultItem.EmptyItem -> holder.getCurrentContext().getString(R.string.empty_items)
            is SearchResultItem.Repository -> item.repository.name
            SearchResultItem.SearchNextItem -> holder.getCurrentContext().getString(R.string.next_page)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }

        // 空のときの表示メッセージのときはクリックできない
        holder.itemView.isClickable = item != SearchResultItem.EmptyItem
    }
}
