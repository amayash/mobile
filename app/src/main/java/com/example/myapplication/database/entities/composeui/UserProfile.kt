package com.example.myapplication.database.entities.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.datastore.DataStoreManager
import com.example.myapplication.datastore.SettingData
import kotlinx.coroutines.launch

@Composable
fun UserProfile(
    isDarkTheme: MutableState<Boolean>,
    dataStoreManager: DataStoreManager
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistration by remember { mutableStateOf(false) }

    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Логин",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                BasicTextField(
                    value = username,
                    onValueChange = { newValue -> username = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(18.dp))
                        .padding(start = 13.dp, top = 8.dp)
                )

                Text(
                    text = "Пароль",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                BasicTextField(
                    value = password,
                    onValueChange = { newValue -> password = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(18.dp))
                        .padding(start = 13.dp, top = 8.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                if (isRegistration) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Регистрация")
                    }
                    Text(
                        text = "Уже есть аккаунт? Войти",
                        modifier = Modifier
                            .clickable {
                                isRegistration = false
                            }
                            .align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Вход")
                    }
                    Text(
                        text = "Нет аккаунта? Зарегистрироваться",
                        modifier = Modifier
                            .clickable {
                                isRegistration = true
                            }
                            .align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                val switchColors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary, // Change the color when the switch is checked
                    checkedTrackColor = MaterialTheme.colorScheme.secondary,  // Change the color of the track when the switch is checked
                    uncheckedThumbColor = MaterialTheme.colorScheme.primary, // Change the color when the switch is unchecked
                    uncheckedTrackColor = MaterialTheme.colorScheme.onPrimary // Change the color of the track when the switch is unchecked
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "Темная тема", modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(5.dp)
                    )

                    val coroutine = rememberCoroutineScope()

                    Switch(
                        checked = isDarkTheme.value,
                        onCheckedChange = {
                            isDarkTheme.value = !isDarkTheme.value
                            coroutine.launch {
                                dataStoreManager.saveSettings(SettingData(isDarkTheme = isDarkTheme.value))
                            }
                        },
                        colors = switchColors
                    )
                }
            }
        }
    }
}

/*@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserProfilePreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            UserProfile(navController = null, isDarkTheme = remember { mutableStateOf(true) })
        }
    }
}*/
