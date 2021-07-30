package org.xapps.apps.foodiex.views.home.servicekey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.databinding.FragmentServiceKeyBinding
import javax.inject.Inject


@AndroidEntryPoint
class ServiceKeyFragment @Inject constructor() : Fragment() {

    private lateinit var bindings: FragmentServiceKeyBinding

    private val viewModel: ServiceKeyViewModel by viewModels()

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
        bindings = FragmentServiceKeyBinding.inflate(layoutInflater)
        return bindings.root
    }

}