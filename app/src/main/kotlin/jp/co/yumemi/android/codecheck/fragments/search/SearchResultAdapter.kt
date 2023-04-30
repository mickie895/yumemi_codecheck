/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.data.RepositoryProperty

val diff_util = object : DiffUtil.ItemCallback<RepositoryProperty>() {
    override fun areItemsTheSame(oldItem: RepositoryProperty, newItem: RepositoryProperty): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RepositoryProperty, newItem: RepositoryProperty): Boolean {
        return oldItem == newItem
    }
}

class SearchResultAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryProperty, SearchResultAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: RepositoryProperty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repositoryProperty = getItem(position)
        (holder.itemView.findViewById<View>(R.id.itemName) as TextView).text =
            repositoryProperty.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(repositoryProperty)
        }
    }
}
