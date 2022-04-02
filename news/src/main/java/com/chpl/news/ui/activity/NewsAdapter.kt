package com.chpl.news.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chpl.news.R
import com.chpl.news.domain.mapper.NewsMapper
import com.chpl.news.domain.model.Article
import com.chpl.news.ui.item.NewsItemHolder
import com.chpl.news.ui.item.NewsItemUiModel

internal class NewsAdapter(
    private val presenter: NewsPresenter
) : RecyclerView.Adapter<NewsItemHolder>() {

    private var newsItems: List<NewsItemUiModel> = emptyList()

    fun update(newsItems: List<NewsItemUiModel>) {
        this.newsItems = newsItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item_view, parent, false)
        return NewsItemHolder(itemView = itemView, presenter = presenter)
    }

    override fun onBindViewHolder(holder: NewsItemHolder, position: Int) = holder.bind(newsItems[position])

    override fun getItemCount() = newsItems.size
}