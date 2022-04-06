package com.chpl.msnews.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.chpl.msnews.App
import com.chpl.msnews.R
import com.chpl.msnews.di.main.DaggerMainComponent
import com.chpl.msnews.ui.news.activity.NewsActivity
import com.chpl.msnews.ui.news.activity.PARAM_KEY_ACCOUNT
import com.chpl.msnews.ui.news.activity.PARAM_KEY_CATEGORIES
import com.chpl.msnews.ui.news.activity.PARAM_KEY_COUNTRIES
import com.chpl.msnews.ui.news.activity.PARAM_KEY_KEYWORDS
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

private const val REQUEST_CODE_SIGN_IN = 3456
// this is not the best practice. it will be moved into separate local file ignored by git
private const val CLIENT_ID = "816572936498-to7raviv7qqubd6edvlv3j3jfrp9f4pn.apps.googleusercontent.com"

class MainActivity : MvpActivity(), MainView {

    @Inject
    lateinit var daggerPresenter: MainPresenter

    @ProvidePresenter
    fun providesPresenter(): MainPresenter = daggerPresenter

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var signInButton: SignInButton
    private lateinit var signInStatus: TextView
    private lateinit var searchButton: Button
    private lateinit var countriesIcon: ImageView
    private lateinit var categoriesIcon: ImageView
    private lateinit var countriesChipGroup: ChipGroup
    private lateinit var categoriesChipGroup: ChipGroup

    private lateinit var signInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
//        (applicationContext as App).applicationComponent.inject(activity = this)
        DaggerMainComponent.factory().createComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initListeners()
        initSignInClient()
    }

    private fun initSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        checkSignInStatus()
    }

    private fun checkSignInStatus() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        presenter.onCheckSignInStatus(account)
    }

    private fun findViews() {
        signInButton = findViewById(R.id.activity_main_sign_in_button)
        signInStatus = findViewById(R.id.activity_main_sign_in_status)
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
            presenter.onSearchClicked()
        }
        signInButton.setOnClickListener {
            presenter.onSignInClicked()
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

    override fun openNewsScreen(keywords: String?, categories: String?, countries: String?, account: String?) {
        val intent = Intent(this, NewsActivity::class.java).apply {
            putExtra(PARAM_KEY_KEYWORDS, keywords)
            putExtra(PARAM_KEY_CATEGORIES, categories)
            putExtra(PARAM_KEY_COUNTRIES, countries)
            putExtra(PARAM_KEY_ACCOUNT, account)
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

    override fun showStatusLoggedOut() {
        signInStatus.text = getString(R.string.sign_in_status_logged_out)
    }

    override fun showStatusLoggedIn(name: String?) {
        signInStatus.text = name?.let { getString(R.string.sign_in_status_logged_in_with_name, it) }
            ?: getString(R.string.sign_in_status_logged_in)
    }

    override fun signIn() {
        startActivityForResult(signInClient.signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun singOut() {
        signInClient.signOut().addOnCompleteListener(this) {
            presenter.onCheckSignInStatus(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                presenter.onCheckSignInStatus(account)
            } catch (e: ApiException) {
                presenter.onCheckSignInStatus(null)
                showError(e)
            }
        }
    }

    override fun showError(it: Throwable) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle(R.string.activity_main_error_dialog_title)
            setMessage(it.message)
            setPositiveButton(R.string.activity_main_error_dialog_pos_button, null)
        }
        builder
            .create()
            .show()
    }
}