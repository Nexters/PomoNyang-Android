package com.pomonyang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pomonyang.data.remote.util.NetworkConnectivityManager
import com.pomonyang.ui.theme.PomonyangTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomonyangTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val isConnected by networkConnectivityManager.connectionFlow.collectAsStateWithLifecycle(
                        networkConnectivityManager.isConnected
                    )

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isConnected) {
                            Greeting(name = "Android")
                        } else {
                            NetworkErrorScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkErrorScreen(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "Network Error",
        color = Color.Red
    )
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "Hello $name!"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PomonyangTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkErrorScreenPreview() {
    PomonyangTheme {
        NetworkErrorScreen()
    }
}
