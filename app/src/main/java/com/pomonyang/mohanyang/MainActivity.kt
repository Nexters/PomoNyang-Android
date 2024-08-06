package com.pomonyang.mohanyang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.ui.MohaNyangApp
import com.pomonyang.mohanyang.ui.rememberMohaNyangAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MnTheme {
                val coroutineScope = rememberCoroutineScope()
                val mohaNyangAppState = rememberMohaNyangAppState(
                    isNewUser = userRepository.isNewUser(),
                    networkMonitor = networkMonitor,
                    coroutineScope = coroutineScope
                )

                MohaNyangApp(mohaNyangAppState = mohaNyangAppState)
            }
        }
    }
}
