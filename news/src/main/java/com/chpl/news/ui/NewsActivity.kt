package com.chpl.news.ui

import android.os.Bundle
import com.chpl.news.R
import com.chpl.news.di.DaggerNewsComponent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerNewsComponent.factory().createComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }
}