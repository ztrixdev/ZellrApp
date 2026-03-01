package ru.ztrixdev.projects.zellrapp

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ztrixdev.projects.zellrapp.stateManagers.LoginStateManager
import ru.ztrixdev.projects.zellrapp.stateManagers.NewListingStateManager

val appModule = module {
    viewModel { LoginStateManager() }
    viewModel { NewListingStateManager() }
}
