package org.xapps.apps.foodiex.views.adapters

import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.models.PopularDrink
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.databinding.ItemPopularDrinkBinding


class PopularDrinkAdapter(
    private val data: ObservableArrayList<PopularDrink>,
    private val listener: Listener
) : ListBindingAdapter<PopularDrink>(data) {

    override val itemLayout: Int
        get() =  R.layout.item_popular_drink

    override fun bind(bindings: ViewDataBinding, item: PopularDrink) {
        debug<PopularDrinkAdapter>("Item binded $item")
        bindings as ItemPopularDrinkBinding
        bindings.recipe = item

        bindings.mcvRoot.setOnClickListener {
            listener.clicked(item)
        }
        bindings.btnBookmark.setOnClickListener {
            if(item.bookmarked.get() == true) {
                debug<RecipeAdapter>("Requesting remove bookmark for $item")
                listener.requestRemoveBookmark(item)
            } else {
                debug<RecipeAdapter>("Requesting insert bookmark for $item")
                listener.requestBookmark(item)
            }
        }
    }

    interface Listener {

        fun clicked(recipe: PopularDrink)

        fun requestBookmark(recipe: PopularDrink)
        fun requestRemoveBookmark(recipe: PopularDrink)

    }
}