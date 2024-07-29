package com.pomonyang.mohanyang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import com.pomonyang.mohanyang.data.remote.util.NetworkMonitor
import com.pomonyang.mohanyang.ui.MohaNyangApp
import com.pomonyang.mohanyang.ui.rememberMohaNyangAppState
import com.pomonyang.mohanyang.ui.theme.MohaNyangTheme
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
            MohaNyangTheme {
                val coroutineScope = rememberCoroutineScope()
                val mohaNyangAppState = rememberMohaNyangAppState(
                    isNewUser = true,
                    networkMonitor = networkMonitor,
                    coroutineScope = coroutineScope
                )

                MohaNyangApp(mohaNyangAppState = mohaNyangAppState)
            }
        }
    }
}
