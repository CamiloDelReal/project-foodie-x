package org.xapps.apps.foodiex.views.home.board

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.exceptions.QuotaHasBeenReachException
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.repositories.RecipesRepository
import org.xapps.apps.foodiex.core.utils.ConnectivityTracker
import org.xapps.apps.foodiex.core.utils.Failure
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.core.utils.error
import org.xapps.apps.foodiex.views.home.recipes.RecipesViewModel
import org.xapps.apps.foodiex.views.utils.Message
import javax.inject.Inject


@HiltViewModel
class BoardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val connectivityTracker: ConnectivityTracker,
    private val recipesRepository: RecipesRepository
): ViewModel() {

    private var boardJob: Job? = null

    private val messageFlow: MutableSharedFlow<Message> = MutableSharedFlow(replay = 1)

    val message: SharedFlow<Message> = messageFlow

    val history = ObservableArrayList<Recipe>()
    val popularDrinks = ObservableArrayList<Recipe>()
    val popularMeals = ObservableArrayList<Recipe>()

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

        connectivityTracker.start()
    }

    fun requestBoard() {
        messageFlow.tryEmit(Message.Loading)
        if(!connectivityTracker.isConnectedToInternet()) {
            messageFlow.tryEmit(Message.InternetNotAvailable)
        }
        boardJob?.cancel()
        boardJob = viewModelScope.launch {
            loadHistory()
            loadPopularDrinks()
            loadPopularMeals()
            messageFlow.tryEmit(Message.Loaded)
        }
    }

    private suspend fun loadHistory() {

    }

    private suspend fun loadPopularDrinks() {
        val resultPopularDrinks = recipesRepository.popularDrinks()
        resultPopularDrinks.either({ failure ->
            error<BoardViewModel>("Error received requesting popular drinks $failure")
            if(failure is Failure.Exception && failure.description is QuotaHasBeenReachException) {
                messageFlow.tryEmit(Message.Error(failure.description))
            }
        }, { recipes ->
            debug<BoardViewModel>("Popular drinks received ${recipes.size}")
            if(recipes.isNotEmpty()) {
                popularDrinks.clear()
                popularDrinks.addAll(recipes)
                messageFlow.tryEmit(Message.PopularDrinksLoaded)
            }
        })
    }

    private suspend fun loadPopularMeals() {
        val resultPopularMeals = recipesRepository.popularMeals()
        resultPopularMeals.either({ failure ->
            error<BoardViewModel>("Error received requesting popular meals $failure")
            if(failure is Failure.Exception && failure.description is QuotaHasBeenReachException) {
                messageFlow.tryEmit(Message.Error(failure.description))
            }
        }, { recipes ->
            debug<BoardViewModel>("Popular meals received ${recipes.size}")
            if(recipes.isNotEmpty()) {
                popularMeals.clear()
                popularMeals.addAll(recipes)
                messageFlow.tryEmit(Message.PopularMealsLoaded)
            }
        })
    }

    fun requestPopulars() {
        messageFlow.tryEmit(Message.Loading)
        boardJob?.cancel()
        boardJob = viewModelScope.launch {
            loadPopularDrinks()
            loadPopularMeals()
            messageFlow.tryEmit(Message.Loaded)
        }
    }

    fun requestBookmark(recipe: Recipe) {
        debug<BoardViewModel>("Resquesting bookmark $recipe")
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
        debug<BoardViewModel>("Resquesting unbookmark $recipe")
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