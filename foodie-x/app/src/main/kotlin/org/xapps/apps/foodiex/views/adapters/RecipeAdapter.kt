package org.xapps.apps.foodiex.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.databinding.ItemRecipeBinding


class RecipeAdapter constructor(
    private val listener: Listener
): PagingDataAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecipeDiffCallBack : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {

        fun clicked(recipe: Recipe)

        fun requestBookmark(recipe: Recipe)
        fun requestRemoveBookmark(recipe: Recipe)

    }

    class RecipeViewHolder(
        private val bindings: ItemRecipeBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(bindings.root) {

        fun bind(recipe: Recipe?) {
            debug<RecipeAdapter>("Binding $recipe")
            recipe?.let {
                bindings.recipe = it

                bindings.mcvRoot.setOnClickListener {
                    listener.clicked(recipe)
                }
                bindings.btnBookmark.setOnClickListener {
                    if(recipe.bookmarked.get() == true) {
                        debug<RecipeAdapter>("Requesting remove bookmark for $recipe")
                        listener.requestRemoveBookmark(recipe)
                    } else {
                        debug<RecipeAdapter>("Requesting insert bookmark for $recipe")
                        listener.requestBookmark(recipe)
                    }
                }
            }
        }

    }

}


