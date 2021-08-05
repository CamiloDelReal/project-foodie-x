package org.xapps.apps.foodiex.views.home.bookmarks

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.models.Recipe
import org.xapps.apps.foodiex.databinding.FragmentBookmarksBinding
import org.xapps.apps.foodiex.views.adapters.RecipeAdapter
import org.xapps.apps.foodiex.views.extensions.showSuccess
import org.xapps.apps.foodiex.views.extensions.showWarning
import org.xapps.apps.foodiex.views.utils.Message
import javax.inject.Inject


@AndroidEntryPoint
class BookmarksFragment @Inject constructor() : Fragment() {

    private lateinit var bindings: FragmentBookmarksBinding
    private val viewModel: BookmarksViewModel by viewModels()

    private lateinit var recipesAdapter: RecipeAdapter
    private val recipesAdapterListener = object: RecipeAdapter.Listener {
        override fun clicked(recipe: Recipe) {
        }

        override fun requestBookmark(recipe: Recipe) {}

        override fun requestRemoveBookmark(recipe: Recipe) {
            viewModel.requestRemoveBookmark(recipe)
        }
    }

    private var paginationJob: Job? = null
    private var paginationStatesJob: Job? = null
    private var messageJob: Job? = null

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
        bindings = FragmentBookmarksBinding.inflate(layoutInflater)

        recipesAdapter = RecipeAdapter(recipesAdapterListener)
        bindings.rvData.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        bindings.rvData.adapter = recipesAdapter

        recipesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                bindings.evNotifications.setDescription(getString(R.string.no_bookmarks))
                bindings.evNotifications.isVisible = (recipesAdapter.itemCount == 0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                bindings.evNotifications.setDescription(getString(R.string.no_bookmarks))
                bindings.evNotifications.isVisible = (recipesAdapter.itemCount == 0)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                bindings.evNotifications.setDescription(getString(R.string.no_bookmarks))
                bindings.evNotifications.isVisible = (recipesAdapter.itemCount == 0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}
        })

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paginationJob = lifecycleScope.launchWhenResumed {
            viewModel.recipes
                .collectLatest { movies ->
                    recipesAdapter.submitData(movies)
                }
        }

        paginationStatesJob = lifecycleScope.launchWhenResumed {
            recipesAdapter.loadStateFlow.collectLatest { loadStates ->
                bindings.progressbar.isVisible = (loadStates.refresh is LoadState.Loading)
            }
        }

        messageJob = lifecycleScope.launchWhenResumed {
            viewModel.message
                .collect {
                    when(it) {
                        is Message.Loading -> {
                            if(recipesAdapter.itemCount == 0) {
                                bindings.evNotifications.setDescription(getString(R.string.loading_bookmarks))
                                bindings.evNotifications.isVisible = true
                            }
                            bindings.progressbar.isVisible = true
                        }
                        is Message.Loaded -> {
                            if(recipesAdapter.itemCount == 0) {
                                bindings.evNotifications.setDescription(getString(R.string.no_bookmarks))
                                bindings.evNotifications.isVisible = true
                            } else {
                                bindings.evNotifications.isVisible = false
                            }
                            bindings.progressbar.isVisible = false
                        }
                        is Message.Error -> {
                            bindings.evNotifications.setDescription(getString(R.string.error_loading_recipes_check_internet))
                            bindings.evNotifications.isVisible = true
                        }
                    }
                }
        }
    }
}