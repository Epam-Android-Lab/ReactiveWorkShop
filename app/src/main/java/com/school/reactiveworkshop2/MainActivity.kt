package com.school.reactiveworkshop2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.school.reactiveworkshop2.databinding.ActivityMainBinding
import com.school.reactiveworkshop2.databinding.ItemActionBinding
import com.school.reactiveworkshop2.databinding.ItemHeaderBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            root.adapter = Adapter()
        }
    }

    private class Adapter : RecyclerView.Adapter<Adapter.BaseHolder>() {
        private val items = listOf(
            Item.Header("МНОГОПОТОЧНОСТЬ🔀"),
            Item.Action("Subscribe_On в начале цепи", ::subscribeOnTop),
            Item.Action("Subscribe_On в середине цепи", ::subscribeOnMiddle),
            Item.Action("Subscribe_On в конце цепи", ::subscribeOnBottom),
            Item.Header("Проблема backpressure\uD83D\uDD19"),
            Item.Action("runNoBackpressure", ::runNoBackpressure),
            Item.Action("runWithBackpressureSingleThread", ::runWithBackpressureSingleThread),
            Item.Action("runWithBackpressureMultithreaded", ::runWithBackpressureMultithread),
            Item.Action("runWithBackpressureMultithreadLowBuffer", ::runWithBackpressureMultithreadLowBuffer),
            Item.Action("runBackpressureSafeRange", ::runBackpressureSafeRange),
            Item.Action("runBackpressureSafeInterval", ::runBackpressureSafeInterval),
            Item.Header("Hot\uD83D\uDD25 and Cold❄"),
            Item.Action("Run Cold", ::runCold),
            Item.Action("Run Publish", ::runPublish),
            Item.Action("Run Replay", ::runReplay),
            Item.Action("Run RefCount", ::runRefCount),
            Item.Header("Subject"),
            Item.Action("Run Subject", ::runSubject),
            Item.Action("Click Subject", clickSubject()),
            Item.Header("Rx в типичном Android приложении"),
            Item.LaunchAction("Запустить", ClassicAppActivity::launch),
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
            return when (viewType) {
                R.layout.item_header -> Header(parent)
                R.layout.item_action -> Action(parent)
                R.layout.activity_classic_app -> LaunchAction(parent)
                else -> throw IllegalStateException("UNKNOWN viewType: $viewType")
            }
        }

        override fun onBindViewHolder(holder: BaseHolder, position: Int) {
            when (val item = items[position]) {
                is Item.Header -> (holder as Header).bind(item)
                is Item.Action -> (holder as Action).bind(item)
                is Item.LaunchAction -> (holder as LaunchAction).bind(item)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun getItemViewType(position: Int): Int {
            return when (items[position]) {
                is Item.Header -> R.layout.item_header
                is Item.Action -> R.layout.item_action
                is Item.LaunchAction -> R.layout.activity_classic_app
            }
        }

        abstract class BaseHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

        private class Header(private val binding: ItemHeaderBinding) : BaseHolder(binding) {
            constructor(parent: ViewGroup) : this(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            fun bind(item: Item.Header) {
                binding.header.text = item.text
            }
        }

        private class Action(private val binding: ItemActionBinding) : BaseHolder(binding) {
            constructor(parent: ViewGroup) : this(ItemActionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            fun bind(item: Item.Action) {
                binding.button.apply {
                    text = item.title
                    setOnClickListener { item.action() }
                }
            }
        }

        private class LaunchAction(private val binding: ItemActionBinding) : BaseHolder(binding) {
            constructor(parent: ViewGroup) : this(ItemActionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            fun bind(item: Item.LaunchAction) {
                binding.button.apply {
                    text = item.title
                    setOnClickListener { item.action(context) }
                }
            }
        }

        sealed class Item {
            data class Header(val text: String) : Item()
            data class Action(val title: String, val action: () -> Unit) : Item()
            data class LaunchAction(val title: String, val action: (Context) -> Unit) : Item()
        }
    }
}
