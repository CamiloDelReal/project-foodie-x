package org.xapps.apps.foodiex.views.home.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import org.xapps.apps.foodiex.core.repositories.SettingsRepository
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    fun isDarkModeOnValue(): Boolean = runBlocking {
        settingsRepository.isDarkModeOnValue()
    }

}