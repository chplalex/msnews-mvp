package com.chpl.msnews.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import com.chpl.msnews.App
import com.chpl.msnews.R
import com.chpl.news.ui.activity.NewsActivity
import com.chpl.news.ui.activity.PARAM_KEY_CATEGORIES
import com.chpl.news.ui.activity.PARAM_KEY_COUNTRIES
import com.chpl.news.ui.activity.PARAM_KEY_KEYWORDS
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

    lateinit var searchButton: Button
    lateinit var countriesIcon: ImageView
    lateinit var categoriesIcon: ImageView
    lateinit var countriesChipGroup: ChipGroup
    lateinit var categoriesChipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initListeners()
    }

    private fun findViews() {
        searchButton = findViewById(R.id.activity_main_search_button)
        countriesIcon = findViewById(R.id.activity_main_countries_icon)
        categoriesIcon = findViewById(R.id.activity_main_categories_icon)
        countriesChipGroup = findViewById(R.id.activity_main_countries_chip_group)
        categoriesChipGroup = findViewById(R.id.activity_main_categories_chip_group)
    }

    private fun initListeners() {
        findViewById<View>(R.id.activity_main_categories).setOnClickListener {
            presenter.onCategoriesClicked()
        }
        findViewById<View>(R.id.activity_main_countries).setOnClickListener {
            presenter.onCountriesClicked()
        }
        findViewById<TextInputEditText>(R.id.activity_main_keywords).addTextChangedListener {
            presenter.onKeywordsChanged(it.toString())
        }
        searchButton.setOnClickListener {
            presenter.onSearchButtonClicked()
        }
    }

    override fun setCategories(categories: List<String>) {
        val container = findViewById<ChipGroup>(R.id.activity_main_categories_chip_group)
        categories.forEach {
            val chip = layoutInflater.inflate(R.layout.filter_item, container, false) as Chip
            chip.text = it
            chip.setOnClickListener { view ->
                view as Chip
                presenter.onCategoryClicked(view.text.toString(), view.isChecked)
            }
            container.addView(chip)
        }
    }

    override fun setCountries(countries: List<String>) {
        val container = findViewById<ChipGroup>(R.id.activity_main_countries_chip_group)
        countries.forEach {
            val chip = layoutInflater.inflate(R.layout.filter_item, container, false) as Chip
            chip.text = it
            chip.setOnClickListener { view ->
                view as Chip
                presenter.onCountryClicked(view.text.toString(), view.isChecked)
            }
            container.addView(chip)
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

    override fun expandCategories() {
        categoriesChipGroup.visibility = VISIBLE
        categoriesIcon.setImageResource(R.drawable.ic_collaps)
    }

    override fun collapseCategories() {
        categoriesChipGroup.visibility = GONE
        categoriesIcon.setImageResource(R.drawable.ic_expand)
    }

    override fun expandCountries() {
        countriesChipGroup.visibility = VISIBLE
        countriesIcon.setImageResource(R.drawable.ic_collaps)
    }

    override fun collapseCountries() {
        countriesChipGroup.visibility = GONE
        countriesIcon.setImageResource(R.drawable.ic_expand)
    }
}