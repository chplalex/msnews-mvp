package com.chpl.msnews.ui.news.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chpl.msnews.App
import com.chpl.msnews.R
import com.chpl.msnews.di.news.DaggerNewsComponent
import com.chpl.msnews.ui.news.item.NewsItemUiModel
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import timber.log.Timber
import javax.inject.Inject

const val PARAM_KEY_KEYWORDS = ".keywords"
const val PARAM_KEY_CATEGORIES = ".categories"
const val PARAM_KEY_COUNTRIES = ".countries"
const val PARAM_KEY_ACCOUNT = ".account"

class NewsActivity : MvpActivity(), NewsView {

    @Inject
    lateinit var daggerPresenter: NewsPresenter

    @ProvidePresenter
    fun providesPresenter(): NewsPresenter = daggerPresenter.apply {
        val keywords = intent.getStringExtra(PARAM_KEY_KEYWORDS)
        val categories = intent.getStringExtra(PARAM_KEY_CATEGORIES)
        val countries = intent.getStringExtra(PARAM_KEY_COUNTRIES)
        val account = intent.getStringExtra(PARAM_KEY_ACCOUNT)
        this.init(keywords, categories, countries, account)
    }

    @InjectPresenter
    lateinit var presenter: NewsPresenter

    private lateinit var progress: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button
    private lateinit var newsLayout: View

    private val recyclerAdapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerNewsComponent.factory().createComponent(this).inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        progress = findViewById(R.id.news_activity_progress)
        newsLayout = findViewById(R.id.news_activity_news_layout)

        recyclerView = findViewById<RecyclerView?>(R.id.news_activity_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        button = findViewById<Button>(R.id.news_activity_favorites_button).apply {
            setOnClickListener { presenter.onButtonClicked() }
        }
    }

    override fun showFavoritesNewsItems(newsItems: List<NewsItemUiModel>) {
        recyclerAdapter.update(newsItems)
        button.text = getString(R.string.news_activity_button_text_news)
    }

    override fun showNewsItems(newsItems: List<NewsItemUiModel>) {
        recyclerAdapter.update(newsItems)
        button.text = getString(R.string.news_activity_button_text_favorites)
    }

    override fun updateNewsItem(position: Int) {
        recyclerAdapter.notifyItemChanged(position)
    }

    override fun removeNewsItem(position: Int) {
        recyclerAdapter.notifyItemRemoved(position)
    }

    override fun showError(it: Throwable) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle(R.string.news_activity_error_dialog_title)
            setMessage(it.message)
            setPositiveButton(R.string.news_activity_error_dialog_pos_button,null)
        }
        builder
            .create()
            .show()
    }

    override fun hideProgress() {
        progress.visibility = GONE
    }

    override fun showProgress() {
        progress.visibility = VISIBLE
    }

    override fun showFavorites() {
        button.visibility = VISIBLE
    }

    override fun hideFavorites() {
        button.visibility = GONE
    }

    override fun openExternalBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            showError(e)
            Timber.e(e)
        }
    }

}