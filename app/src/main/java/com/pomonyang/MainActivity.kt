package com.pomonyang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import com.pomonyang.data.remote.util.NetworkMonitor
import com.pomonyang.ui.PomoNyangApp
import com.pomonyang.ui.rememberPomoNyangAppState
import com.pomonyang.ui.theme.PomonyangTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomonyangTheme {
                val coroutineScope = rememberCoroutineScope()
                val pomoNyangAppState = rememberPomoNyangAppState(
                    isNewUser = true,
                    networkMonitor = networkMonitor,
                    coroutineScope = coroutineScope
                )

                PomoNyangApp(pomoNyangAppState = pomoNyangAppState)
            }
        }
    }
}
