package org.xapps.apps.foodiex

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import org.xapps.apps.foodiex.core.repositories.SettingsRepository
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class FoodiexApplication: MultiDexApplication() {

    @Inject lateinit var settingsRepository: SettingsRepository

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            AppCompatDelegate.setDefaultNightMode(if (settingsRepository.isDarkModeOnValue()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}