package org.xapps.apps.foodiex.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.xapps.apps.foodiex.core.repositories.SettingsRepository
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    fun isDarkModeOn(): Flow<Boolean> = settingsRepository.isDarkModeOn()

    fun isDarkModeOnValue(): Boolean = runBlocking {
        settingsRepository.isDarkModeOnValue()
    }

    fun setIsDarkModeOn(isDarkModeOn: Boolean) {
        viewModelScope.launch {
            settingsRepository.setIsDarkModeOn(isDarkModeOn)
        }
    }

}