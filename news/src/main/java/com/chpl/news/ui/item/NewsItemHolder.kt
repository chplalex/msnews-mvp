package com.chpl.news.ui.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chpl.news.R
import com.chpl.news.ui.activity.NewsPresenter

internal class NewsItemHolder(
    itemView: View,
    private val presenter: NewsPresenter
) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.article_item_view_title)
    private val author = itemView.findViewById<TextView>(R.id.article_item_view_author)
    private val description = itemView.findViewById<TextView>(R.id.article_item_view_description)
    private val image = itemView.findViewById<ImageView>(R.id.article_item_view_image)
    private val favoriteIcon = itemView.findViewById<ImageView>(R.id.article_item_view_favorite_icon)

    fun bind(itemUiModel: NewsItemUiModel) {
        title.text = itemUiModel.title
        author.text = itemUiModel.author
        description.text = itemUiModel.description

        val requestImageOptions = RequestOptions()
            .fitCenter()
            .error(R.drawable.ic_image_placeholder)
            .placeholder(R.drawable.ic_image_placeholder)
        Glide.with(image.context)
            .load(itemUiModel.imageUrl)
            .apply(requestImageOptions)
            .into(image)

        val resId = if (itemUiModel.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        favoriteIcon.setImageResource(resId)
        favoriteIcon.setOnClickListener { presenter.onFavoriteIconClicked(itemUiModel.id) }
    }
}