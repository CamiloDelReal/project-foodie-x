package org.xapps.apps.foodiex.views.bindings

import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.modules.RecipesModule


object ImageViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["recipeId", "recipeImageExtension"], requireAll = true)
    fun recipePicture(view: ImageView, recipeId: Long, recipeImageExtension: String) {
        Glide
            .with(view.context)
            .load("${RecipesModule.BASE_URL_IMAGES}${recipeId}-${RecipesModule.DIMENSION_636x393}.${recipeImageExtension}")
            .placeholder(if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) R.drawable.ic_progress_download else R.drawable.ic_progress_download_dark)
            .error(if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) R.drawable.ic_image_broken else R.drawable.ic_image_broken_dark)
            .centerCrop()
            .into(view)
    }

}