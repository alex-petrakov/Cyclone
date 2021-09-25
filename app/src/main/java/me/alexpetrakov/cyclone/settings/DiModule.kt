package me.alexpetrakov.cyclone.settings

import me.alexpetrakov.cyclone.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel {
        SettingsViewModel(
            router = get()
        )
    }
}