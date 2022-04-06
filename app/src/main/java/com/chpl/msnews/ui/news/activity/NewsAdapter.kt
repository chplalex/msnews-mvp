package com.chpl.msnews.ui.news.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chpl.msnews.R
import com.chpl.msnews.ui.news.item.NewsItemHolder
import com.chpl.msnews.ui.news.item.NewsItemUiModel

internal class NewsAdapter : RecyclerView.Adapter<NewsItemHolder>() {

    private var newsItems: List<NewsItemUiModel> = emptyList()

    fun update(newsItems: List<NewsItemUiModel>) {
        this.newsItems = newsItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item_view, parent, false)
        return NewsItemHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: NewsItemHolder, position: Int) = holder.bind(newsItems[position])

    override fun getItemCount() = newsItems.size
}