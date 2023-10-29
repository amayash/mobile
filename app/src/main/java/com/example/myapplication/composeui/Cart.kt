package com.example.myapplication.composeui

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.model.SessionFromCart
import com.example.myapplication.ui.theme.PmudemoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun Cart(id: Int) {
    val context = LocalContext.current
    val sessions = remember { mutableStateListOf<SessionFromCart>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).userDao().getCartByUid(id).collect { data ->
                sessions.clear()
                sessions.addAll(data)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(all = 10.dp)
    ) {
        items(sessions) { session ->
            var currentCount by remember { mutableStateOf(session.count) }

            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val formattedDate = dateFormatter.format(session.dateTime)

            Text(
                text = formattedDate,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (session.cinema.image != null)
                        Image(
                            bitmap = BitmapFactory.decodeByteArray(
                                session.cinema.image,
                                0,
                                session.cinema.image.size
                            ).asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .padding(4.dp)
                        )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${session.cinema.name}, ${session.cinema.year}\n" +
                                    "Цена: ${session.price}\n" +
                                    "${currentCount}/${session.availableCount}",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(10.dp)
                            ) // Задаем фон для кнопок
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (currentCount > 0) {
                                        currentCount--
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.minus),
                                    contentDescription = "Уменьшить",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(10.dp)
                                )
                            }

                            Text(
                                text = "$currentCount",
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            IconButton(
                                onClick = {
                                    if (currentCount < session.availableCount) {
                                        currentCount++
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Увеличить",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    Column {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) { Text("Купить") }
    }
}


@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CartPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Cart(1)
        }
    }
}
