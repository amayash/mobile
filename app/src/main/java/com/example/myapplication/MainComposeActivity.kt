package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.myapplication.composeui.navigation.MainNavbar
import com.example.myapplication.datastore.DataStoreManager
import com.example.myapplication.ui.theme.PmudemoTheme

//import com.jakewharton.threetenabp.AndroidThreeTen

class MainComposeActivity : ComponentActivity() {
    private val dataStoreManager = DataStoreManager(this)
    private val isDarkTheme = mutableStateOf(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.deleteDatabase("pmy-db")
        //AndroidThreeTen.init(this)
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