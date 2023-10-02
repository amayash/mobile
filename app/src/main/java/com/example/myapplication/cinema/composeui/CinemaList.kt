package com.example.myapplication.cinema.composeui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.cinema.model.getCinemas
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.ui.theme.Gray
import com.example.myapplication.ui.theme.PmudemoTheme

@Composable
fun CinemaList(navController: NavController?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp)
    ) {
        items(getCinemas().indices.toList()) { index ->
            val cinema = getCinemas()[index]
            val cinemaId = Screen.CinemaView.route.replace("{id}", index.toString())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp)
                    .clickable { navController?.navigate(cinemaId) }
                    .background(
                        color = Gray,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = cinema.image),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )

                    Text("${cinema.name}, ${cinema.year}")
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CinemaListPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CinemaList(navController = null)
        }
    }
}