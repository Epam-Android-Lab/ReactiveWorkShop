package com.school.reactiveworkshop2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.jakewharton.rxbinding4.view.clicks
import com.school.reactiveworkshop2.databinding.ActivityClassicAppBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ClassicAppActivity : AppCompatActivity() {

    private val viewModel by viewModels<ClassicViewModel>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityClassicAppBinding.inflate(layoutInflater).apply {
            setContentView(root)

            compositeDisposable.add(button.clicks().subscribe(viewModel.getNameObserver::onNext))

            viewModel.name.observe(this@ClassicAppActivity) {
                textView.text = it
            }

            viewModel.loading.observe(this@ClassicAppActivity) {
                button.isEnabled = it.not()
                progressBar.isVisible = it
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        fun launch(context: Context) = context.startActivity(Intent(context, ClassicAppActivity::class.java))
    }
}
