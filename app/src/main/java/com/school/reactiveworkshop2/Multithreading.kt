package com.school.reactiveworkshop2

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private val observable = Observable.interval(1, TimeUnit.SECONDS).take(1)

fun subscribeOnTop() {
    observable
        .doOnSubscribe {
            Log.d("🍕", "Subscribed on ${Thread.currentThread().name}")
        }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map {
            Log.d("🍕", "Mapped on ${Thread.currentThread().name}")
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { Log.d("🍕", "Observe result on ${Thread.currentThread().name}") }
}

fun subscribeOnMiddle() {
    observable
        .doOnSubscribe { Log.d("🍕", "Subscribed on ${Thread.currentThread().name}") }
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation())
        .map {
            Log.d("🍕", "Mapped on ${Thread.currentThread().name}")
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { Log.d("🍕", "Observe result on ${Thread.currentThread().name}") }
}

fun subscribeOnBottom() {
    observable
        .doOnSubscribe { Log.d("🍕", "Subscribed on ${Thread.currentThread().name}") }
        .observeOn(Schedulers.computation())
        .map {
            Log.d("🍕", "Mapped on ${Thread.currentThread().name}")
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe { Log.d("🍕", "Observe result on ${Thread.currentThread().name}") }
}

