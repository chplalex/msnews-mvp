package com.chpl.news.ui.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chpl.news.R
import com.chpl.news.domain.model.Article

class NewsItemHolder(
    itemView: View,
    private val presenter: NewsItemPresenter
) : RecyclerView.ViewHolder(itemView), NewsItemView {

    private val title = itemView.findViewById<TextView>(R.id.article_item_view_title)
    private val author = itemView.findViewById<TextView>(R.id.article_item_view_author)
    private val description = itemView.findViewById<TextView>(R.id.article_item_view_description)
    private val image = itemView.findViewById<ImageView>(R.id.article_item_view_image)
    private val favoriteIcon = itemView.findViewById<ImageView>(R.id.article_item_view_favorite_icon)

    fun bind(article: Article) {
        title.text = article.title
        author.text = article.author
        description.text = article.description
    }

    override fun setFavoriteIconOn() {
        favoriteIcon.setImageResource(R.drawable.ic_favorite_on)
    }

    override fun setFavoriteIconOff() {
        favoriteIcon.setImageResource(R.drawable.ic_favorite_off)
    }
}