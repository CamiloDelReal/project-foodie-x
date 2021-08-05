package org.xapps.apps.foodiex.views.adapters

import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.models.PopularDrink
import org.xapps.apps.foodiex.core.models.PopularMeal
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.databinding.ItemPopularDrinkBinding
import org.xapps.apps.foodiex.databinding.ItemPopularMealBinding


class PopularMealAdapter(
    private val data: ObservableArrayList<PopularMeal>,
    private val listener: Listener
) : ListBindingAdapter<PopularMeal>(data) {

    override val itemLayout: Int
        get() =  R.layout.item_popular_meal

    override fun bind(bindings: ViewDataBinding, item: PopularMeal) {
        debug<PopularMealAdapter>("Item binded $item")
        bindings as ItemPopularMealBinding
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

        fun clicked(recipe: PopularMeal)

        fun requestBookmark(recipe: PopularMeal)
        fun requestRemoveBookmark(recipe: PopularMeal)

    }

}