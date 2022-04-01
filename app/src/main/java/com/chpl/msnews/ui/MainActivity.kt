package com.chpl.msnews.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.chpl.msnews.App
import com.chpl.msnews.R
import com.chpl.news.ui.NewsActivity
import com.chpl.news.ui.PARAM_KEY_CATEGORIES
import com.chpl.news.ui.PARAM_KEY_COUNTRIES
import com.chpl.news.ui.PARAM_KEY_KEYWORDS
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MainActivity : MvpActivity(), MainView {

    @Inject
    lateinit var daggerPresenter: MainPresenter

    @ProvidePresenter
    fun providesPresenter(): MainPresenter = daggerPresenter

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setKeywordsInput()
        setSearchButton()
    }

    private fun setKeywordsInput() {
        findViewById<TextInputEditText>(R.id.activity_main_keywords).addTextChangedListener {
            presenter.onKeywordsChanged(it.toString())
        }
    }

    private fun setSearchButton() {
        findViewById<View>(R.id.activity_main_search_button).setOnClickListener {
            presenter.onSearchButtonClicked()
        }
    }

    override fun setCategories(categories: List<String>) {
        val container = findViewById<ChipGroup>(R.id.activity_main_categories_chip_group)
        categories.forEach {
            val chip = layoutInflater.inflate(R.layout.filter_item, container, false) as Chip
            chip.text = it
            container.addView(chip)
        }
        container.setOnCheckedChangeListener { _, checkedId ->
            val chip = container.findViewById<Chip>(checkedId)
            presenter.onCategoryClicked(chip.text.toString(), chip.isChecked)
        }
    }

    override fun setCountries(countries: List<String>) {
        val container = findViewById<ChipGroup>(R.id.activity_main_countries_chip_group)
        countries.forEach {
            val chip = layoutInflater.inflate(R.layout.filter_item, container, false) as Chip
            chip.text = it
            container.addView(chip)
        }
        container.setOnCheckedChangeListener { _, checkedId ->
            val chip = container.findViewById<Chip>(checkedId)
            presenter.onCountryClicked(chip.text.toString(), chip.isChecked)
        }
    }

    override fun openNewsScreen(keywords: String?, categories: String?, countries: String?) {
        val intent = Intent(this, NewsActivity::class.java).apply {
            putExtra(PARAM_KEY_KEYWORDS, keywords)
            putExtra(PARAM_KEY_CATEGORIES, categories)
            putExtra(PARAM_KEY_COUNTRIES, countries)
        }
        startActivity(intent)
    }
}