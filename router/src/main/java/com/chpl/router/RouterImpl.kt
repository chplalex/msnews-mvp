package com.chpl.router

import timber.log.Timber

internal class RouterImpl: Router {

    override fun openNewsScreen(
        keywords: String,
        categories: List<String>,
        countries: List<String>
    ) {
        Timber.d("openNewsScreen")
    }
}