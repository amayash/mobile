package com.example.myapplication.user.composeui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.composeui.navigation.Screen
import com.example.myapplication.session.composeui.SessionList
import com.example.myapplication.ui.theme.Gray
import com.example.myapplication.ui.theme.PmudemoTheme

@Composable
fun UserProfile(navController: NavController?, switchDarkTheme: () -> Unit, isDarkTheme: Boolean) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistration by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Логин",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onBackground
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
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onBackground
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
        val switchColors = if (isDarkTheme) {
            SwitchDefaults.colors(
                checkedThumbColor = Color.White, // Change the color when the switch is checked
                checkedTrackColor = Color.Gray,  // Change the color of the track when the switch is checked
                uncheckedThumbColor = Color.Gray, // Change the color when the switch is unchecked
                uncheckedTrackColor = Color.LightGray // Change the color of the track when the switch is unchecked
            )
        } else {
            SwitchDefaults.colors(
                checkedThumbColor = Color.Black, // Change the color when the switch is checked
                checkedTrackColor = Color.Gray,  // Change the color of the track when the switch is checked
                uncheckedThumbColor = Color.Gray, // Change the color when the switch is unchecked
                uncheckedTrackColor = Color.LightGray // Change the color of the track when the switch is unchecked
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text("Dark Theme", modifier = Modifier.align(Alignment.CenterVertically).padding(5.dp), color = MaterialTheme.colorScheme.onBackground)

            Switch(
                checked = isDarkTheme,
                onCheckedChange = {
                    switchDarkTheme()
                },
                colors = switchColors
            )
        }
    }
}


@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserProfilePreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            UserProfile(navController = null, switchDarkTheme = {}, isDarkTheme = true)
        }
    }
}
