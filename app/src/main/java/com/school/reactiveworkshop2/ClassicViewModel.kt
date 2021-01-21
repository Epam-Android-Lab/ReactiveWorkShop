package com.school.reactiveworkshop2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class ClassicViewModel : ViewModel() {
    private val getNameSubject = PublishSubject.create<Unit>()

    init {
        getNameSubject
            .doOnNext { _loading.postValue(true) }
            .switchMap { ClassicRepository.getName().onErrorReturn { it.message }.toObservable() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _loading.postValue(false)
                _name.value = it
            }
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val getNameObserver: Observer<Unit> = getNameSubject
}
