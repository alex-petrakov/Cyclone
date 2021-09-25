package me.alexpetrakov.cyclone.settings.presentation

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router

class SettingsViewModel(private val router: Router) : ViewModel() {
    fun onNavigateBack() {
        router.exit()
    }
}
