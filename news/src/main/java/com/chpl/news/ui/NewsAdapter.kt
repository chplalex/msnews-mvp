package com.chpl.news.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chpl.news.R
import com.chpl.news.domain.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsViewHolder>() {

    private var articles: List<Article> = emptyList()

    fun update(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item_view, parent, false)
        return NewsViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) = holder.bind(articles[position])

    override fun getItemCount() = articles.size
}