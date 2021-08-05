package org.xapps.apps.foodiex.views.home.search

import android.animation.Animator
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.R
import org.xapps.apps.foodiex.core.utils.debug
import org.xapps.apps.foodiex.databinding.FragmentSearchBinding
import javax.inject.Inject


@AndroidEntryPoint
class SearchBottomSheetFragment @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var bindings: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var bottomSheet: ViewGroup
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun getTheme(): Int {
        return R.style.Theme_BottomSheet
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        bindings = FragmentSearchBinding.inflate(layoutInflater)
        dialog.setContentView(bindings.root)
        bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheetBehavior = BottomSheetBehavior.from(bindings.root.parent as View)
        bottomSheetBehavior.peekHeight = (resources.displayMetrics.heightPixels * 0.6).toInt()
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        debug<SearchBottomSheetFragment>("Bottom sheet state expanded")
                        setExpandedState()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        debug<SearchBottomSheetFragment>("Bottom sheet state collapsed")
                        setCollapsedState()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                }
            }

        })


        return dialog
    }

    private fun setExpandedState() {
        TransitionManager.beginDelayedTransition(bottomSheet)
        bindings.clToolbar.isVisible = true
        bindings.btnSearch.isVisible = false
    }

    private fun setCollapsedState() {
        TransitionManager.beginDelayedTransition(bottomSheet)
        bindings.clToolbar.isVisible = false
        bindings.btnSearch.isVisible = true
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

}