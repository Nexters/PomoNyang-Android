package com.pomonyang.data.remote.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface NetworkConnectivityManager {
    val isConnected: Boolean
    val connectionFlow: Flow<Boolean>
}

@SuppressLint("MissingPermission")
internal class NetworkConnectivityManagerImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context
    ) : NetworkConnectivityManager {
        private val connectivityManager by lazy {
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        }

        override val connectionFlow: Flow<Boolean> =
            callbackFlow {
                val networkCallback =
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onLost(network: Network) {
                            trySend(false).isSuccess
                        }

                        override fun onCapabilitiesChanged(
                            network: Network,
                            networkCapabilities: NetworkCapabilities
                        ) {
                            val isConnected =
                                networkCapabilities.run {
                                    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                                        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                                }
                            trySend(isConnected).isSuccess
                        }
                    }

                connectivityManager.registerDefaultNetworkCallback(networkCallback)
                awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
            }

        override val isConnected: Boolean
            get() =
                connectivityManager.activeNetwork?.let { network ->
                    connectivityManager.getNetworkCapabilities(network)?.run {
                        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    }
                } ?: false
    }
