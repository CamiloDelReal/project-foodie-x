package org.xapps.apps.foodiex.views.home.searchresults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.xapps.apps.foodiex.databinding.FragmentSearchResultsBinding
import javax.inject.Inject


@AndroidEntryPoint
class SearchResultsFragment @Inject constructor() : Fragment() {

    private lateinit var bindings: FragmentSearchResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentSearchResultsBinding.inflate(layoutInflater)
        return bindings.root
    }

}