package org.xapps.apps.foodiex.views.home.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.databinding.FragmentBoardBinding
import org.xapps.apps.foodiex.databinding.ItemItemBinding
import javax.inject.Inject


@AndroidEntryPoint
class BoardFragment @Inject constructor(): Fragment() {

    private lateinit var bindings: FragmentBoardBinding

    private lateinit var adapter: ItemAdapter
    private var data: ObservableArrayList<Item> = ObservableArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            duration = 200
        }
        reenterTransition = MaterialFadeThrough().apply {
            duration = 200
        }
        exitTransition = MaterialFadeThrough().apply {
            duration = 200
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentBoardBinding.inflate(layoutInflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..1000) {
            data.add(Item(data = "Item $i"))
        }
        adapter = ItemAdapter(data)
        bindings.rlData.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        bindings.rlData.adapter = adapter
    }
}


data class Item(
    val data: String
)


abstract class ListBindingAdapter<TItem>(items: List<TItem>) :
    RecyclerView.Adapter<ListBindingAdapter.BindingViewHolder>() {

    var items: List<TItem> = ArrayList()
        set(value) {
            val lastList = field
            if (lastList !== value) {
                if (lastList is ObservableList) {
                    lastList.removeOnListChangedCallback(onListChangedCallback)
                }
                if (value is ObservableList) {
                    value.addOnListChangedCallback(onListChangedCallback)
                }
                if (lastList.isNotEmpty()) {
                    notifyItemRangeRemoved(0, lastList.size)
                }
            }
            field = value
            notifyItemRangeInserted(0, value.size)
        }

    class BindingViewHolder constructor(val bindings: ViewDataBinding) :
        RecyclerView.ViewHolder(bindings.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val bindings = createBinding(parent)
        return BindingViewHolder(bindings)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun createBinding(parent: ViewGroup): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            itemLayout,
            parent,
            false
        )
    }

    abstract val itemLayout: Int

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        if (position < itemCount) {
            bind(holder.bindings, getItem(position))
            holder.bindings.executePendingBindings()
        }
    }

    private fun getItem(position: Int): TItem {
        return items[position]
    }

    protected abstract fun bind(bindings: ViewDataBinding, item: TItem)

    private val onListChangedCallback =
        object : ObservableList.OnListChangedCallback<ObservableList<TItem>>() {
            override fun onChanged(sender: ObservableList<TItem>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<TItem>?,
                positionStart: Int,
                itemCount: Int
            ) {
//                notifyItemRangeRemoved(positionStart, itemCount)
                notifyDataSetChanged()
            }

            override fun onItemRangeMoved(
                sender: ObservableList<TItem>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
//                notifyItemMoved(fromPosition, toPosition)
                notifyDataSetChanged()
            }

            override fun onItemRangeInserted(
                sender: ObservableList<TItem>?,
                positionStart: Int,
                itemCount: Int
            ) {
//                notifyItemRangeChanged(positionStart, itemCount)
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                sender: ObservableList<TItem>?,
                positionStart: Int,
                itemCount: Int
            ) {
//                notifyItemRangeChanged(positionStart, itemCount)
                notifyDataSetChanged()
            }
        }

    init {
        this.items = items
    }
}

class ItemAdapter(
    private val data: ObservableArrayList<Item>
) : ListBindingAdapter<Item>(data) {

    override val itemLayout: Int
        get() =  R.layout.item_item

    override fun bind(bindings: ViewDataBinding, item: Item) {
        debug<BoardFragment>("Item binded $item")
        when (bindings) {

            is ItemItemBinding -> {
                debug<ItemAdapter>("ItemItemBinding detected")
            }
        }
    }

}