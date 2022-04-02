package com.chpl.news.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chpl.news.R
import com.chpl.news.domain.model.Article
import com.chpl.news.ui.item.NewsItemHolder

class NewsAdapter : RecyclerView.Adapter<NewsItemHolder>() {

    private var articles: List<Article> = emptyList()

    fun update(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item_view, parent, false)
        return NewsItemHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: NewsItemHolder, position: Int) = holder.bind(articles[position])

    override fun getItemCount() = articles.size
}