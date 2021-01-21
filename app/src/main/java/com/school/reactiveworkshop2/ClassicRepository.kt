package com.school.reactiveworkshop2

import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object ClassicRepository {

    fun getName(): Single<String> = ClassicRemoteDataSource.getName()

    object ClassicRemoteDataSource {
        private val names = mapOf(
            0 to "Jonas",
            1 to "Reptile",
            2 to "Lemmy",
            3 to "Greg",
            4 to "Chet",
            5 to "Hill"
        )

        fun getName(): Single<String> = Single.just(Unit)
            .delay(1000, TimeUnit.MILLISECONDS)
            .map { names[Random.nextInt(0, 10)] ?: throw IllegalStateException("Network Error!") }
    }
}
