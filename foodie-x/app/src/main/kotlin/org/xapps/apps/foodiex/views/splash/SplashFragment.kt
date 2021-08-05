package org.xapps.apps.foodiex.views.splash

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.databinding.FragmentSplashBinding
import org.xapps.apps.foodiex.views.extensions.*
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment @Inject constructor() : Fragment() {

    private lateinit var bindings: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentSplashBinding.inflate(layoutInflater)

        val theme = requireContext().theme
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val colorPrimary = typedValue.data
        navigationBarColor(colorPrimary)

        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            context?.let {
                requestPermissionsWithDexter(
                    permissions = listOf(Manifest.permission.INTERNET),
                    permissionsGranted = {
                        exitTransition = Hold()
                        bindings.clRoot.transitionName = getString(R.string.transition_splash_to_home)
                        val extras = FragmentNavigatorExtras(bindings.clRoot to getString(R.string.transition_splash_to_home))
                        findNavController().navigate(SplashFragmentDirections.actionFragmentSplashToFragmentHome(), extras)
                    },
                    permissionsDenied = {
                        showWarning(getString(R.string.internet_permission_denied))
                        requireActivity().finish()
                    },
                    error = {
                        showError(getString(R.string.error_requesting_permission))
                    }
                )


            }
        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarForegoundColor(isLightStatusBar = false)
    }

}