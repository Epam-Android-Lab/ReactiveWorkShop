package com.school.reactiveworkshop2

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

//Cold

private val observable = Observable.interval(1, TimeUnit.SECONDS).take(5).subscribeOn(Schedulers.io())

fun runCold() {
    observable.subscribe { Log.d("🍕Cold1", "$it") }
    observable.subscribe { Log.d("🍕Cold2", "$it") }
}

//Hot

//publish
val publishObservable = observable.publish()

fun runPublish() {
    publishObservable.connect()
    publishObservable.subscribe { Log.d("🍕Publish1", "$it") }
    Thread.sleep(4000)
    publishObservable.subscribe { Log.d("🍕Publish2", "$it") }
}

//replay
val replayObservable = observable.replay()

fun runReplay() {
    replayObservable.connect()
    replayObservable.subscribe { Log.d("🍕Replay1", "$it") }
    Thread.sleep(4000)
    replayObservable.subscribe { Log.d("🍕Replay2", "$it") }
}

//refCount
val refCountObservable = observable.publish().refCount()

fun runRefCount() {
    val compositeDisposable = CompositeDisposable()
    val disposable1 = refCountObservable.subscribe { Log.d("🍕RefCount1", "$it") }
    compositeDisposable.add(disposable1)
    Thread.sleep(4000)
    val disposable2 = refCountObservable.subscribe { Log.d("🍕RefCount2", "$it") }
    compositeDisposable.add(disposable2)
    Thread.sleep(2000)
    compositeDisposable.dispose()
    Thread.sleep(4000)
    refCountObservable.subscribe { Log.d("🍕3", "$it") }
}

//Subject

val publishSubject = ReplaySubject.create<Long>()

fun runSubject() {
    publishSubject
        .subscribeOn(Schedulers.io())
        .subscribe(
            { Log.d("🍕Subject", "$it") },
            {},
            { Log.d("🍕Subject", "Completed") },
        )
    observable.map { it - 5 }.subscribe(publishSubject)
    observable.map { it + 5 }.subscribe(publishSubject)
}

fun clickSubject(): () -> Unit {
    publishSubject.subscribe { Log.d("🍕Subject", "$it") }
    return {
        publishSubject.onNext(Random.nextLong())
    }
}


