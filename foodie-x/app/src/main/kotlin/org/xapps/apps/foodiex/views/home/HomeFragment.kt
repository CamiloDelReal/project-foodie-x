package org.xapps.apps.foodiex.views.home

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.skydoves.whatif.whatIfNotNullAs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.utils.error
import org.xapps.apps.foodiex.core.utils.info
import org.xapps.apps.foodiex.databinding.FragmentHomeBinding
import org.xapps.apps.foodiex.views.extensions.navigationBarColor
import org.xapps.apps.foodiex.views.extensions.setFadeInText
import org.xapps.apps.foodiex.views.extensions.setStatusBarForegoundColor
import org.xapps.apps.foodiex.views.extensions.uncheckAllItems
import org.xapps.apps.foodiex.views.navigation.MainNavigator
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment @Inject constructor(): Fragment(), MainNavigator {

    private lateinit var bindings: FragmentHomeBinding

    private lateinit var innerNavController: NavController

    private val viewModel: HomeViewModel by viewModels()

    private var darkModeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.animation_duration_normal).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentHomeBinding.inflate(layoutInflater)

        val theme = requireContext().theme
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSurface, typedValue, true)
        val colorSurface = typedValue.data
        navigationBarColor(colorSurface)

        ViewCompat.setOnApplyWindowInsetsListener(bindings.mlRoot) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            val statusBarLayoutParams = bindings.vStatusBarBg.layoutParams
            statusBarLayoutParams.height = statusBarHeight
            bindings.vStatusBarBg.layoutParams = statusBarLayoutParams

            val navigationBarLayoutParams = bindings.vNavigationBarBg.layoutParams
            navigationBarLayoutParams.height = navigationBarHeight
            bindings.vNavigationBarBg.layoutParams = navigationBarLayoutParams

            ViewCompat.onApplyWindowInsets(v, insets)
        }

        innerNavController = (childFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment).navController

        innerNavController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.fragment_board -> {
                    bindings.txvTitle.setFadeInText(getString(R.string.app_name))
                }
                R.id.fragment_recipes -> {
                    bindings.txvTitle.setFadeInText(getString(R.string.title_recipes))
                }
                R.id.fragment_bookmarks -> {
                    bindings.txvTitle.setFadeInText(getString(R.string.title_bookmarks))
                }
                R.id.fragment_search_results -> {
                    bindings.txvTitle.setFadeInText(getString(R.string.title_search_results))
                }
                R.id.fragment_service_key -> {
                    bindings.txvTitle.setFadeInText(getString(R.string.title_service_key))
                    bindings.bottomNavigationView.uncheckAllItems()
                }
            }
        }

        NavigationUI.setupWithNavController(bindings.bottomNavigationView, innerNavController)

        bindings.btnSearch.setOnClickListener {
            openSearch()
        }

        bindings.btnMoreOptions.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_service_key -> {
                            openServiceKey()
                            true
                        }
                        R.id.action_dark_mode -> {
                            info<HomeFragment>("Dark mode configuration will change. Flow collector will handle it")
                            viewModel.setIsDarkModeOn(!viewModel.isDarkModeOnValue())
                            true
                        }
                        R.id.action_about -> {
                            info<HomeFragment>("Open About view request received")
                            openAbout()
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.menu_home_more_options)
                if(viewModel.isDarkModeOnValue()) {
                    val item = menu.findItem(R.id.action_dark_mode)
                    item.isChecked = true
                }
                show()
            }
        }

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        darkModeJob = lifecycleScope.launchWhenResumed {
            viewModel.isDarkModeOn()
                .catch { ex->
                    error<HomeFragment>(ex)
                }
                .collect { isDarkModeOn ->
                    info<HomeFragment>("Using dark mode flow collector")
                    AppCompatDelegate.setDefaultNightMode(if (isDarkModeOn) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                }
        }
    }

    private fun openSearch() {
        findNavController().navigate(HomeFragmentDirections.actionFragmentHomeToFragmentSearch())
    }

    override fun openServiceKey() {
        innerNavController.navigate(R.id.fragment_service_key)
    }

    override fun openRecipeDetails(id: Long) {
        findNavController().navigate(HomeFragmentDirections.actionFragmentHomeToDetailsFragment())
    }

    private fun openAbout() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.animation_duration_normal).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.animation_duration_normal).toLong()
        }
        bindings.mlRoot.transitionName = getString(R.string.transition_home_to_about)
        val extras = FragmentNavigatorExtras(bindings.mlRoot to getString(R.string.transition_home_to_about))
        findNavController().navigate(HomeFragmentDirections.actionFragmentHomeToFragmentAbout(), extras)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarForegoundColor(isLightStatusBar = false)
    }

}
