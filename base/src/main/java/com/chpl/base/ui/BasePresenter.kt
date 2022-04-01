package com.chpl.base.ui

import androidx.annotation.CallSuper
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

open class BasePresenter<View : MvpView?> : MvpPresenter<View>() {

    private val compositeDisposable = CompositeDisposable()

    protected fun unsubscribeOnDestroy(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun clearSubscriptions() {
        compositeDisposable.clear()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        clearSubscriptions()
    }
}