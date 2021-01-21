package com.school.reactiveworkshop2

import android.util.Log
import io.reactivex.rxjava3.core.BackpressureOverflowStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private val observable = Observable.range(1, 20)

// Проблема backpressure отсутвует
fun runNoBackpressure() {
    observable.subscribe(
        {
            Log.d("🍕⏭", "onNext: $it")
        },
        {
            Log.d("🍕❌", "onError: $it")
        },
        {
            Log.d("🍕🏁", "onComplete")
        }
    )
}

//Проблема backpressure присутствует но из-за однопоточности, она неочевидна
fun runWithBackpressureSingleThread() {
    observable.subscribeOn(Schedulers.computation())
        .doOnNext { Log.d("🍕⏭", "Next Posted: $it") }
        .subscribe(
            {
                Log.d("🍕⏭", "onNext: $it")
                try {
                    TimeUnit.MILLISECONDS.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            },
            {
                Log.d("🍕❌", "onError: $it")
            },
            {
                Log.d("🍕🏁", "onComplete")
            }
        )
}

//Проблема backpressure присутствует, но скрыт за неограниченным буфером observeOn
fun runWithBackpressureMultithread() {
    observable.subscribeOn(Schedulers.computation())
        .doOnNext { Log.d("🍕⏭", "Next Posted: $it") }
        .observeOn(Schedulers.io())
        .subscribe(
            {
                Log.d("🍕⏭", "onNext: $it")
                try {
                    TimeUnit.MILLISECONDS.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            },
            {
                Log.d("🍕❌", "onError: $it")
            },
            {
                Log.d("🍕🏁", "onComplete")
            }
        )
}

// Попытка получить ошибку при переполнении буфера
fun runWithBackpressureMultithreadLowBuffer() {
    observable.subscribeOn(Schedulers.computation())
        .doOnNext { Log.d("🍕⏭", "Next Posted: $it") }
        .observeOn(Schedulers.io(), false, 16)
        .subscribe(
            {
                Log.d("🍕⏭", "onNext: $it")
                try {
                    TimeUnit.MILLISECONDS.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            },
            {
                Log.d("🍕❌", "onError: $it")
            },
            {
                Log.d("🍕🏁", "onComplete")
            }
        )
}

// Идеальный код с точки зрения backpressure
fun runBackpressureSafeRange() {
    Flowable.range(1, 30)
        .subscribeOn(Schedulers.computation())
        .doOnNext { Log.d("🍕⏭", "Next Posted: $it") }
        .observeOn(Schedulers.io(), false, 3)
        .subscribe(
            {
                Log.d("🍕⏭", "onNext: $it")
                try {
                    TimeUnit.MILLISECONDS.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            },
            {
                Log.d("🍕❌", "onError: $it")
            },
            {
                Log.d("🍕🏁", "onComplete")
            }
        )
}

// Решенная проблема backpressure
fun runBackpressureSafeInterval() {
    Flowable.interval(100, TimeUnit.MILLISECONDS)
        .take(30)
        .doOnNext { Log.d("🍕⏭", "Next Posted to Buffer: $it") }
        .onBackpressureBuffer(10, { Log.d("🍕❌", "Ooops Overflow. Will drop oldest") }, BackpressureOverflowStrategy.DROP_OLDEST)
        .doOnNext { Log.d("🍕⏭", "Next Posted from Buffer: $it") }
        .subscribeOn(Schedulers.computation())
        .observeOn(Schedulers.io(), false, 3)
        .subscribe(
            {
                Log.d("🍕⏭", "onNext: $it")
                try {
                    TimeUnit.MILLISECONDS.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            },
            {
                Log.d("🍕❌", "onError: $it")
            },
            {
                Log.d("🍕🏁", "onComplete")
            }
        )
}

