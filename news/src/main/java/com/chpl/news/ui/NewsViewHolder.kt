package com.chpl.news.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chpl.news.R
import com.chpl.news.domain.model.Article

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.article_item_view_title)
    private val author = itemView.findViewById<TextView>(R.id.article_item_view_author)
    private val description = itemView.findViewById<TextView>(R.id.article_item_view_description)
    private val image = itemView.findViewById<ImageView>(R.id.article_item_view_image)

    fun bind(article: Article) {
        title.text = article.title
        author.text = article.author
        description.text = article.description
    }
}