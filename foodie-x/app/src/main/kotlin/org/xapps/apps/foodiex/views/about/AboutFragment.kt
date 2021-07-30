package org.xapps.apps.foodiex.views.about

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.BuildConfig
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.databinding.FragmentAboutBinding
import org.xapps.apps.foodiex.views.extensions.launchUri
import org.xapps.apps.foodiex.views.extensions.navigationBarColor
import org.xapps.apps.foodiex.views.extensions.setStatusBarForegoundColor
import javax.inject.Inject


@AndroidEntryPoint
class AboutFragment @Inject constructor() : Fragment() {

    private lateinit var bindings: FragmentAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.animation_duration_normal).toLong()
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.animation_duration_normal).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentAboutBinding.inflate(layoutInflater)
        bindings.version = BuildConfig.VERSION_NAME

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

        bindings.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        bindings.btnAboutLink.setOnClickListener {
            launchUri(getString(R.string.project_github))
        }

        bindings.btnLinkGoogleFonts.setOnClickListener {
            launchUri(getString(R.string.noto_sans_url))
        }

        bindings.btnLinkMaterialDesignIcons.setOnClickListener {
            launchUri(getString(R.string.material_design_icons_url))
        }

        bindings.btnLinkAndroidJetpack.setOnClickListener {
            launchUri(getString(R.string.android_jetpack_url))
        }

        bindings.btnLinkAndroidKotlin.setOnClickListener {
            launchUri(getString(R.string.android_kotlin_url))
        }

        bindings.btnLinkDexterPermissions.setOnClickListener {
            launchUri(getString(R.string.dexter_url))
        }

        bindings.btnLinkWhatIf.setOnClickListener {
            launchUri(getString(R.string.whatif_url))
        }

        bindings.btnLinkToasty.setOnClickListener {
            launchUri(getString(R.string.toasty_url))
        }

        bindings.btnLinkTimber.setOnClickListener {
            launchUri(getString(R.string.timber_url))
        }

        return bindings.root
    }

    override fun onResume() {
        super.onResume()
        setStatusBarForegoundColor(isLightStatusBar = false)
    }

}