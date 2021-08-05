package org.xapps.apps.foodiex.views.home.recipes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.repositories.RecipesRepository
import org.xapps.apps.foodiex.core.utils.ConnectivityTracker
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.core.utils.error
import org.xapps.apps.foodiex.views.home.bookmarks.BookmarksViewModel
import org.xapps.apps.foodiex.views.utils.Message
import javax.inject.Inject


@HiltViewModel
class RecipesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val connectivityTracker: ConnectivityTracker,
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val messageFlow: MutableSharedFlow<Message> = MutableSharedFlow(replay = 1)
    private val recipesFlow: MutableSharedFlow<PagingData<Recipe>> = MutableSharedFlow(replay = 1)

    val message: SharedFlow<Message> = messageFlow
    val recipes: SharedFlow<PagingData<Recipe>> = recipesFlow

    private var wasInternetLastTimeAvailable = true

    init {
        wasInternetLastTimeAvailable = connectivityTracker.isConnectedToInternet()
        if(!wasInternetLastTimeAvailable) {
            messageFlow.tryEmit(Message.InternetNotAvailable)
        }

        viewModelScope.launch {
            connectivityTracker.connectivityFlow
                .collect { available ->
                    debug<RecipesViewModel>("Connectivity availavility = $available")
                    if (wasInternetLastTimeAvailable != available) {
                        if (available) {
                            messageFlow.tryEmit(Message.InternetAvailable)
                        } else {
                            messageFlow.tryEmit(Message.InternetNotAvailable)
                        }
                        wasInternetLastTimeAvailable = available
                    }
                }
        }

        viewModelScope.launch{
            recipesRepository.recipes().cachedIn(viewModelScope)
                .catch { ex ->
                    error<RecipesViewModel>(ex, "Exception captured")
                    messageFlow.emit(Message.Error(Exception(ex.localizedMessage)))
                }
                .collectLatest { pagingData ->
                    val pagingDataWithBookmarks = pagingData.map { recipe ->
                        val result = recipesRepository.checkBookmark(recipe)
                        result.either({ failure ->
                            error<RecipesViewModel>("Error received $failure")
                        }, { bookmarked ->
                        })
                        recipe
                    }

                    recipesFlow.emit(pagingDataWithBookmarks)
                    messageFlow.emit(Message.Loaded)
                }
        }

        connectivityTracker.start()
    }

    fun requestBookmark(recipe: Recipe) {
        viewModelScope.launch {
            val result = recipesRepository.insertBookmark(recipe)
            result.either({ failure ->
                error<RecipesViewModel>("Error received $failure")
                messageFlow.tryEmit(Message.Error(Exception(context.getString(R.string.error_bookmarking_recipe))))
            }, { bookmark ->
                debug<RecipesViewModel>("Bookmark created")
            })
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

    fun isInternetAvailable(): Boolean {
        return connectivityTracker.isConnectedToInternet()
    }

}