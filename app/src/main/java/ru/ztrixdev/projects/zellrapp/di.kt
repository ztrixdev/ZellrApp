package ru.ztrixdev.projects.zellrapp

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.ztrixdev.projects.zellrapp.stateManagers.LoginStateManager
import ru.ztrixdev.projects.zellrapp.stateManagers.NewListingStateManager
import ru.ztrixdev.projects.zellrapp.stateManagers.ProfileSetupStateManager
import ru.ztrixdev.projects.zellrapp.stateManagers.UserProfileStateManager

val appModule = module {
    viewModel { LoginStateManager() }
    viewModel { NewListingStateManager() }
    viewModel { ProfileSetupStateManager() }
    viewModel { UserProfileStateManager() }
}
