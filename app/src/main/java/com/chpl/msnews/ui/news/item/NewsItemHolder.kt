package com.chpl.msnews.ui.news.item

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chpl.msnews.R

internal class NewsItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val container = itemView.findViewById<View>(R.id.article_item_view_container)
    private val title = itemView.findViewById<TextView>(R.id.article_item_view_title)
    private val author = itemView.findViewById<TextView>(R.id.article_item_view_author)
    private val description = itemView.findViewById<TextView>(R.id.article_item_view_description)
    private val image = itemView.findViewById<ImageView>(R.id.article_item_view_image)
    private val favoriteIcon = itemView.findViewById<ImageView>(R.id.article_item_view_favorite_icon)

    fun bind(itemUiModel: NewsItemUiModel) {
        title.text = itemUiModel.title
        author.text = itemUiModel.author
        description.text = itemUiModel.description

        val requestOptions = RequestOptions()
            .fitCenter()
            .error(R.drawable.ic_image_placeholder)
            .placeholder(R.drawable.ic_image_placeholder)
        Glide.with(image.context)
            .load(itemUiModel.imageUrl)
            .apply(requestOptions)
            .into(image)

        val (fiResId, fiVisibility) = when (itemUiModel.favoriteState) {
            com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_ON -> R.drawable.ic_favorite_on to VISIBLE
            com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_OFF -> R.drawable.ic_favorite_off to VISIBLE
            com.chpl.msnews.domain.source.favorites.FavoriteState.DISABLED -> R.drawable.ic_favorite_disabled to GONE
        }
        favoriteIcon.apply {
            setImageResource(fiResId)
            setOnClickListener { itemUiModel.onFavoriteAction?.invoke(itemUiModel.id) }
            visibility = fiVisibility
        }

        container.setOnClickListener { itemUiModel.onItemAction?.invoke(itemUiModel.articleUrl) }
    }
}