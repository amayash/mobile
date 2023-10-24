package com.example.myapplication.entities.composeui

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entities.model.OrderWithSessions
import com.example.myapplication.ui.theme.PmudemoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun OrderView(id: Int) {
    val context = LocalContext.current
    val (orderWithSessions, setOrderWithSessions) = remember {
        mutableStateOf<OrderWithSessions?>(null)
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            setOrderWithSessions(AppDatabase.getInstance(context).orderSessionCrossRefDao().getByUid(id))
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        if (orderWithSessions != null) {
            items(orderWithSessions.sessions) { session ->
                val count = remember { mutableStateOf(0) }
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        count.value = AppDatabase.getInstance(context).orderSessionCrossRefDao().getCountOfSessionsInOrder(id, session.uid ?: 0)
                    }
                }
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
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${session.cinema.name}, ${session.cinema.year}\n" +
                                        "Количество: ${count.value}",
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderViewPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            OrderView(id = 1)
        }
    }
}