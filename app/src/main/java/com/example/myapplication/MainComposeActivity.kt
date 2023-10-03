package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.composeui.navigation.MainNavbar
import com.example.myapplication.datastore.DataStoreManager
import com.example.myapplication.ui.theme.PmudemoTheme

class MainComposeActivity : ComponentActivity() {
    private val dataStoreManager = DataStoreManager(this)
    private val isDarkTheme = mutableStateOf(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PmudemoTheme(darkTheme = isDarkTheme.value) {
                LaunchedEffect(key1 = true) {
                    dataStoreManager.getSettings().collect { setting ->
                        isDarkTheme.value = setting.isDarkTheme
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavbar(
                        isDarkTheme = isDarkTheme,
                        dataStoreManager = dataStoreManager
                    )
                }
            }
        }

    }
}
/*
@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainNavbarPreview() {
    val dataStoreManager = DataStoreManager(MainComposeActivity)
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            MainNavbar(remember { mutableStateOf(true) })
        }
    }
}*/
