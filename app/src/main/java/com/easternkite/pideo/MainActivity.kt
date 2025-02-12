package com.easternkite.pideo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.easternkite.pideo.core.network.PideoApi
import com.easternkite.pideo.core.ui.theme.PideoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var api: PideoApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PideoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var response by remember { mutableStateOf("") }
                    LaunchedEffect(Unit) {
                        val result = api.searchImage(
                            query = "개발자",
                            sort = "recency",
                            page = 1,
                            size = 20
                        )

                        if (result.isSuccessful) {
                            response = result.body()?.documents?.firstOrNull()?.docUrl ?: ""
                        }
                    }
                    Greeting(
                        name = response,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PideoTheme {
        Greeting("Android")
    }
}