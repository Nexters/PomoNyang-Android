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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.startup.AppInitializer
import app.rive.runtime.kotlin.RiveInitializer
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.notification.LocalNotificationReceiver
import com.pomonyang.mohanyang.notification.util.createNotificationChannel
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButton
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonColorType
import com.pomonyang.mohanyang.presentation.designsystem.button.box.MnBoxButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.dialog.MnDialog
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

@OptIn(DelicateCoroutinesApi::class)
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
                coroutineScope = coroutineScope
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
                if (showDialog) NetworkFailDialog(
                    onClickRefresh = {
                        viewModel.handleEvent(MainEvent.ClickRefresh)
                    }, onDismissRequest = {
                        viewModel.handleEvent(MainEvent.ClickClose)
                    }
                )
                else AppScreen(viewState = state, mohaNyangAppState = mohaNyangAppState)
            }
        }

    }

    @Composable
    fun AppScreen(viewState: MainState, mohaNyangAppState: MohaNyangAppState) {
        when {
            viewState.isLoading -> LoadingScreen()
            viewState.isError -> ErrorScreen()
            else -> {
                MohaNyangApp(mohaNyangAppState = mohaNyangAppState)
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        }
    }

    @Composable
    fun ErrorScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("에러발생!")
        }
    }

    @Composable
    fun NetworkFailDialog(
        onClickRefresh: () -> Unit,
        onDismissRequest: () -> Unit,
    ) {
        MnDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = true
            ),
            title = getString(R.string.dialog_network_title),
            subTitle = getString(R.string.dialog_network_content),
            positiveButton = {
                MnBoxButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = getString(R.string.dialog_network_refresh),
                    onClick = onClickRefresh,
                    colors = MnBoxButtonColorType.primary,
                    styles = MnBoxButtonStyles.medium,

                    )
            },
            onDismissRequest = onDismissRequest
        )
    }


    private fun configureAppStartup() {
        AppInitializer.getInstance(applicationContext)
            .initializeComponent(RiveInitializer::class.java)

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


    companion object {
        private const val SPLASH_DELAY = 2000L
    }
}
