package org.xapps.apps.foodiex.views.home.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.exceptions.QuotaHasBeenReachException
import org.xapps.apps.foodiex.core.models.PopularDrink
import org.xapps.apps.foodiex.core.models.PopularMeal
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.databinding.FragmentBoardBinding
import org.xapps.apps.foodiex.views.adapters.PopularDrinkAdapter
import org.xapps.apps.foodiex.views.adapters.PopularMealAdapter
import org.xapps.apps.foodiex.views.extensions.showSuccess
import org.xapps.apps.foodiex.views.extensions.showWarning
import org.xapps.apps.foodiex.views.home.recipes.RecipesFragment
import org.xapps.apps.foodiex.views.utils.Message
import javax.inject.Inject


@AndroidEntryPoint
class BoardFragment @Inject constructor(): Fragment() {

    private lateinit var bindings: FragmentBoardBinding
    private val viewModel: BoardViewModel by viewModels()

    private var messageJob: Job? = null

    private lateinit var popularDrinksAdapter: PopularDrinkAdapter

    private val popularDrinksAdapterListener = object: PopularDrinkAdapter.Listener {
        override fun clicked(recipe: Recipe) {
        }

        override fun requestBookmark(recipe: Recipe) {
            viewModel.requestBookmark(recipe)
        }

        override fun requestRemoveBookmark(recipe: Recipe) {
            viewModel.requestRemoveBookmark(recipe)
        }
    }

    private lateinit var popularMealsAdapter: PopularMealAdapter

    private val popularMealsAdapterListener = object: PopularMealAdapter.Listener {
        override fun clicked(recipe: Recipe) {
        }

        override fun requestBookmark(recipe: Recipe) {
            viewModel.requestBookmark(recipe)
        }

        override fun requestRemoveBookmark(recipe: Recipe) {
            viewModel.requestRemoveBookmark(recipe)
        }
    }

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

        popularDrinksAdapter = PopularDrinkAdapter(viewModel.popularDrinks, popularDrinksAdapterListener)
        bindings.rvPopularDrinks.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        bindings.rvPopularDrinks.adapter = popularDrinksAdapter

        popularMealsAdapter = PopularMealAdapter(viewModel.popularMeals, popularMealsAdapterListener)
        bindings.rvPopularMeals.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        bindings.rvPopularMeals.adapter = popularMealsAdapter

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageJob = lifecycleScope.launchWhenResumed {
            viewModel.message.collect {
                debug<BoardFragment>("Message received $it")
                when(it) {
                    is Message.Loading -> {
                        bindings.progressbar.isVisible = true
                    }
                    is Message.Loaded -> {
                        bindings.progressbar.isVisible = false
                    }
                    is Message.Error -> {
                        if(it.exception is QuotaHasBeenReachException) {
                            debug<BoardFragment>("Quota reached")
                            bindings.evNotifications.setDescription(getString(R.string.quota_reached))
                            bindings.evNotifications.isVisible = true
                        }
                    }
                    is Message.InternetAvailable -> {
                        bindings.evNotifications.isVisible = false
                        showSuccess(getString(R.string.internet_available_loading_from_web))
                        viewModel.requestPopulars()
                    }
                    is Message.InternetNotAvailable -> {
                        if(viewModel.history.isEmpty() && viewModel.popularDrinks.isEmpty() && viewModel.popularMeals.isEmpty()) {
                            bindings.evNotifications.setDescription(getString(R.string.error_loading_board_check_internet))
                            bindings.evNotifications.isVisible = true
                        }
                        showWarning(getString(R.string.internet_not_available_loading_from_cache))
                    }
                    Message.HistoryLoaded -> {
                        debug<BoardFragment>("History loaded")
                        bindings.evNotifications.isVisible = false
                        bindings.txvHistory.isVisible = true
                        bindings.rvHistory.isVisible = true
                    }
                    Message.PopularDrinksLoaded -> {
                        debug<BoardFragment>("Popular drinks loaded")
                        bindings.evNotifications.isVisible = false
                        bindings.txvPopularDrinks.isVisible = true
                        bindings.rvPopularDrinks.isVisible = true
                    }
                    Message.PopularMealsLoaded -> {
                        debug<BoardFragment>("Popular meals loaded")
                        bindings.evNotifications.isVisible = false
                        bindings.txvPopularMeals.isVisible = true
                        bindings.rvPopularMeals.isVisible = true

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(!viewModel.isInternetAvailable()) {
            if(viewModel.history.isEmpty() && viewModel.popularDrinks.isEmpty() && viewModel.popularMeals.isEmpty()) {
                bindings.evNotifications.setDescription(getString(R.string.error_loading_board_check_internet))
                bindings.evNotifications.isVisible = true
            } else {
                bindings.evNotifications.isVisible = false
                showWarning(getString(R.string.internet_not_available_loading_from_cache))
            }
        } else {
            if(viewModel.history.isEmpty() && viewModel.popularDrinks.isEmpty() && viewModel.popularMeals.isEmpty()) {
                bindings.evNotifications.setDescription(getString(R.string.loading_board))
                bindings.evNotifications.isVisible = true
            } else {
                bindings.evNotifications.isVisible = false
            }
        }

        viewModel.requestBoard()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        messageJob?.cancel()
        messageJob = null
    }
}
