package com.easternkite.pideo

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class PdConnectivity(private val context: Context) {
    enum class NetworkState {
        CONNECTED,
        UNAVAILABLE,
        DISCONNECTED,
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val connectivityManager
        get() = getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager

    val networkState = callbackFlow {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(NetworkState.CONNECTED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(NetworkState.DISCONNECTED)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(NetworkState.UNAVAILABLE)
            }
        }.also {
            connectivityManager.registerNetworkCallback(networkRequest, it)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(it)
            }
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Main),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NetworkState.UNAVAILABLE,
    )
}

@Composable
fun rememberPdConnectivity(): PdConnectivity {
    val context = LocalContext.current
    return remember {
        PdConnectivity(context = context)
    }
}

@Composable
fun ConnectivityContainer(
    modifier: Modifier = Modifier,
    connectivity: PdConnectivity = rememberPdConnectivity(),
    onChanged: (PdConnectivity.NetworkState) -> Unit = {},
    indicator: @Composable BoxScope.() -> Unit = {
        val state by connectivity.networkState.collectAsState()
        ConnectivityIndicator(modifier = Modifier.fillMaxWidth(), state = state)
    },
    content: @Composable BoxScope.() -> Unit
) {
    val state by connectivity.networkState.collectAsState()
    LaunchedEffect(state) {
        onChanged(state)
    }

    Box(modifier) {
        content()
        indicator()
    }
}

@Composable
fun BoxScope.ConnectivityIndicator(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    colors: ConnectivityColors = ConnectivityColors.Default,
    style: TextStyle = TextStyle.Default,
    isVisible: Boolean = true,
    state: PdConnectivity.NetworkState = PdConnectivity.NetworkState.CONNECTED
) {
    val backgroundColor = when (state) {
        PdConnectivity.NetworkState.CONNECTED -> colors.connectedBackgroundColor
        PdConnectivity.NetworkState.DISCONNECTED -> colors.disconnectedBackgroundColor
        PdConnectivity.NetworkState.UNAVAILABLE -> colors.unAvailableBackGroundColor
    }

    val textColor = when (state) {
        PdConnectivity.NetworkState.CONNECTED -> colors.connectedTextColor
        PdConnectivity.NetworkState.DISCONNECTED -> colors.disconnectedTextColor
        PdConnectivity.NetworkState.UNAVAILABLE -> colors.unAvailableTextColor
    }

    val text = when (state) {
        PdConnectivity.NetworkState.CONNECTED -> "Connected"
        PdConnectivity.NetworkState.DISCONNECTED -> "Disconnected"
        PdConnectivity.NetworkState.UNAVAILABLE -> "Unavailable"
    }

    AnimatedVisibility(
        modifier = modifier.align(alignment),
        visible = isVisible
    ) {
        Text(
            text = "Network is currently $text",
            modifier = Modifier.background(backgroundColor).padding(5.dp),
            style = style.copy(textColor),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ConnectivityIndicatorPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        ConnectivityIndicator(
            alignment = Alignment.BottomStart,
            modifier = Modifier.fillMaxWidth(),
            state = PdConnectivity.NetworkState.CONNECTED
        )
    }
}

data class ConnectivityColors(
    val connectedBackgroundColor: Color,
    val disconnectedBackgroundColor: Color,
    val unAvailableBackGroundColor: Color,
    val connectedTextColor: Color = Color.White,
    val disconnectedTextColor: Color = Color.White,
    val unAvailableTextColor: Color = Color.White
) {
    companion object {
        val Default = ConnectivityColors(
            connectedBackgroundColor = Color(0xFF4CAF50),
            disconnectedBackgroundColor = Color(0xFFB3261E),
            unAvailableBackGroundColor = Color(0xFFB3261E)
        )
    }
}