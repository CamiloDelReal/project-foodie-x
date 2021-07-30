package org.xapps.apps.foodiex.views.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty
import org.xapps.apps.foodiex.R


fun Fragment.showError(message: String) {
    Toasty.custom(
        requireContext(),
        message,
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_alert_outline
        ),
        ContextCompat.getColor(requireContext(), R.color.error),
        ContextCompat.getColor(requireContext(), R.color.white),
        Toasty.LENGTH_LONG,
        true,
        true
    ).show()
}

fun Fragment.showSuccess(message: String) {
    Toasty.custom(
        requireContext(),
        message,
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_check_circle_outline
        ),
        ContextCompat.getColor(requireContext(), R.color.success),
        ContextCompat.getColor(requireContext(), R.color.white),
        Toasty.LENGTH_SHORT,
        true,
        true
    ).show()
}

fun Fragment.showWarning(message: String) {
    Toasty.custom(
        requireContext(),
        message,
        AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_alert_circle_outline
        ),
        ContextCompat.getColor(requireContext(), R.color.warning),
        ContextCompat.getColor(requireContext(), R.color.white),
        Toasty.LENGTH_SHORT,
        true,
        true
    ).show()
}

fun Fragment.hideKeyboard(views: List<View?>) {
    val inputMethodManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    views.forEach { view ->
        view?.let {
            if (it.hasFocus()) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                return@forEach
            }
        }
    }
}

fun Fragment.setStatusBarForegoundColor(isLightStatusBar: Boolean) {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        val decorView = requireActivity().window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        if(isLightStatusBar) {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    } else {
        val statusBarAppearance = if(isLightStatusBar) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0
        requireActivity().window.insetsController?.setSystemBarsAppearance(statusBarAppearance, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
    }
}

fun Fragment.navigationBarColor(color: Int) {
    requireActivity().window.navigationBarColor = color
}



fun Fragment.launchUri(uri: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    startActivity(intent)
}