package org.xapps.apps.foodiex.views.home.bookmarks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.repositories.RecipesRepository
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.core.utils.error
import org.xapps.apps.foodiex.views.home.recipes.RecipesViewModel
import org.xapps.apps.foodiex.views.utils.Message
import javax.inject.Inject


@HiltViewModel
class BookmarksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recipesRepository: RecipesRepository
): ViewModel() {

    private val messageFlow: MutableSharedFlow<Message> = MutableSharedFlow(replay = 1)
    private val recipesFlow: MutableSharedFlow<PagingData<Recipe>> = MutableSharedFlow(replay = 1)

    val message: SharedFlow<Message> = messageFlow
    val recipes: SharedFlow<PagingData<Recipe>> = recipesFlow

    init {
        viewModelScope.launch{
            recipesRepository.recipesBookmarkedPaginated().cachedIn(viewModelScope)
                .catch { ex ->
                    error<BookmarksViewModel>(ex, "Exception captured")
                    messageFlow.emit(Message.Error(Exception(ex.localizedMessage)))
                }
                .collectLatest { pagingData ->
                    val pagingDataWithBookmarks = pagingData.map { recipe ->
                        recipe.bookmarked.set(true)
                        recipe
                    }

                    recipesFlow.emit(pagingDataWithBookmarks)
                    messageFlow.emit(Message.Loaded)
                }
        }
    }

    fun requestRemoveBookmark(recipe: Recipe) {
        viewModelScope.launch {
            val result = recipesRepository.removeBookmark(recipe)
            result.either({ failure ->
                error<RecipesViewModel>("Error received $failure")
                messageFlow.tryEmit(Message.Error(Exception(context.getString(R.string.error_unbookmarking_recipe))))
            }, { bookmark ->
                debug<RecipesViewModel>("Bookmark removed")
            })
        }
    }

}