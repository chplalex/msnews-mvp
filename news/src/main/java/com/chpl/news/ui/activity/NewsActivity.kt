package com.chpl.news.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chpl.news.R
import com.chpl.news.di.DaggerNewsComponent
import com.chpl.news.domain.model.Article
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

const val PARAM_KEY_KEYWORDS = ".keywords"
const val PARAM_KEY_CATEGORIES = ".categories"
const val PARAM_KEY_COUNTRIES = ".countries"

class NewsActivity : MvpActivity(), NewsView {

    @Inject
    lateinit var daggerPresenter: NewsPresenter

    @ProvidePresenter
    fun providesPresenter(): NewsPresenter = daggerPresenter.apply {
        val keywords = intent.getStringExtra(PARAM_KEY_KEYWORDS)
        val categories = intent.getStringExtra(PARAM_KEY_CATEGORIES)
        val countries = intent.getStringExtra(PARAM_KEY_COUNTRIES)
        this.init(keywords, categories, countries)
    }

    @InjectPresenter
    lateinit var presenter: NewsPresenter

    private lateinit var progress: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button
    private val recyclerAdapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerNewsComponent.factory().createComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        progress = findViewById(R.id.news_activity_progress)

        recyclerView = findViewById<RecyclerView?>(R.id.news_activity_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        button = findViewById<Button>(R.id.news_activity_favorites_button).apply {
            setOnClickListener { presenter.onButtonClicked() }
        }
    }

    override fun showFavoritesArticles(articles: List<Article>) {
        recyclerAdapter.update(articles)
        button.text = getString(R.string.news_activity_button_text_news)
    }

    override fun showNewsArticles(articles: List<Article>) {
        recyclerAdapter.update(articles)
        button.text = getString(R.string.news_activity_button_text_favorites)
    }

    override fun hideProgress() {
        progress.visibility = GONE
    }

    override fun showProgress() {
        progress.visibility = VISIBLE
    }
}