package org.xapps.apps.foodiex.views.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.databinding.FragmentDetailsBinding
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment @Inject constructor() : Fragment() {

    private lateinit var bindings: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentDetailsBinding.inflate(layoutInflater)
        return bindings.root
    }

}