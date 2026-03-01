package ru.ztrixdev.projects.zellrapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.jan.supabase.auth.status.SessionStatus
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.dgis.sdk.DGis
import ru.ztrixdev.projects.zellrapp.screens.LoadingScreen
import ru.ztrixdev.projects.zellrapp.screens.LoginScreen
import ru.ztrixdev.projects.zellrapp.screens.MainScreen
import ru.ztrixdev.projects.zellrapp.network.auth.SessionManager
import ru.ztrixdev.projects.zellrapp.ui.theme.ZellrAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lateinit var sdkContext: ru.dgis.sdk.Context

        sdkContext = DGis.initialize(
            this
        )


        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }
        setContent {
            ZellrAppTheme {
                var status: SessionStatus? by remember { mutableStateOf(null) }

                LaunchedEffect(Unit) {
                    SessionManager.getSessionStatus().collect {
                        status = it
                    }
                }

                when (status) {
                    is SessionStatus.Authenticated -> MainScreen(dgisctx = sdkContext)
                    is SessionStatus.NotAuthenticated -> LoginScreen()
                    is SessionStatus.RefreshFailure -> LoginScreen()
                    is SessionStatus.Initializing -> LoadingScreen()
                    null -> LoadingScreen()
                }
            }
        }

    }


}
