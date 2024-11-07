package com.pomonyang.mohanyang

import android.app.Activity
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.rememberNavController
import androidx.startup.AppInitializer
import app.rive.runtime.kotlin.RiveInitializer
import com.datadog.android.compose.ExperimentalTrackingApi
import com.datadog.android.compose.NavigationViewTrackingEffect
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.notification.LocalNotificationReceiver
import com.pomonyang.mohanyang.notification.util.createNotificationChannel
import com.pomonyang.mohanyang.notification.util.deleteNotificationChannelIfExists
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants
import com.pomonyang.mohanyang.presentation.screen.common.LoadingScreen
import com.pomonyang.mohanyang.presentation.screen.common.NetworkErrorDialog
import com.pomonyang.mohanyang.presentation.screen.common.NetworkErrorScreen
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import com.pomonyang.mohanyang.ui.MohaNyangApp
import com.pomonyang.mohanyang.ui.MohaNyangAppState
import com.pomonyang.mohanyang.ui.rememberMohaNyangAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class, ExperimentalTrackingApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        handleSplashScreen()
        super.onCreate(savedInstanceState)

        configureAppStartup()

        setContent {
            val activity = (LocalContext.current as? Activity)

            val state by viewModel.state.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            val mohaNyangAppState = rememberMohaNyangAppState(
                isNewUser = viewModel.checkIfNewUser(),
                networkMonitor = networkMonitor,
                coroutineScope = coroutineScope,
                navHostController = rememberNavController().apply {
                    NavigationViewTrackingEffect(navController = this)
                }
            )

            var showDialog by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                viewModel.handleEvent(MainEvent.Init)
            }

            viewModel.effects.collectWithLifecycle { effect ->
                when (effect) {
                    MainEffect.ShowDialog -> {
                        showDialog = true
                    }

                    MainEffect.DismissDialog -> {
                        showDialog = false
                    }

                    MainEffect.ExitApp -> {
                        activity?.finish()
                    }

                    MainEffect.GoToTimer -> {}

                    MainEffect.GoToOnBoarding -> {}


                }
            }

            MnTheme {
                if (showDialog) NetworkErrorDialog(
                    onClickRefresh = {
                        viewModel.handleEvent(MainEvent.ClickRefresh)
                    }, onDismissRequest = {
                        viewModel.handleEvent(MainEvent.ClickClose)
                    }
                )
                else AppScreen(
                    modifier = Modifier,
                    viewState = state,
                    mohaNyangAppState = mohaNyangAppState
                )
            }
        }

    }

    @Composable
    fun AppScreen(
        modifier: Modifier,
        viewState: MainState,
        mohaNyangAppState: MohaNyangAppState,
    ) {
        when {
            viewState.isLoading -> LoadingScreen(modifier = modifier)
            viewState.isError -> NetworkErrorScreen(modifier = modifier, onClickRetry = {})
            else -> {
                MohaNyangApp(mohaNyangAppState = mohaNyangAppState)
            }
        }
    }


    private fun configureAppStartup() {
        AppInitializer.getInstance(applicationContext)
            .initializeComponent(RiveInitializer::class.java)

        deletePrevNotificationChannel()
        createNotificationChannel()
        registerNotificationService()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
    }


    private fun handleSplashScreen() {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            keepSplashOnScreen = false
        }
    }

    override fun onResume() {
        super.onResume()
        MnNotificationManager.stopInterrupt(this)
    }

    override fun onDestroy() {
        GlobalScope.launch { viewModel.updateRecentPomodoroDoneData() }
        super.onDestroy()
    }


    private fun registerNotificationService() {
        val intentFilters = IntentFilter().apply {
            MnNotificationManager.intents.map { addAction(it) }
        }
        val receiver = LocalNotificationReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilters)
    }

    private fun deletePrevNotificationChannel() {
        val deprecatedNotificationChannel = listOf(getString(R.string.channel_id), PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_ID)

        deprecatedNotificationChannel.forEach { deleteNotificationChannelIfExists(it) }

    }


    companion object {
        private const val SPLASH_DELAY = 2000L
    }
}
