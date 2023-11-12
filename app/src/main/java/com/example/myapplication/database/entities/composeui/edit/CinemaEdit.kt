package com.example.myapplication.database.entities.composeui.edit

import android.content.res.Configuration
import android.util.Base64
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.database.entities.composeui.AppViewModelProvider
import com.example.myapplication.database.entities.model.Cinema
import com.example.myapplication.database.entities.model.SessionFromCinema
import com.example.myapplication.ui.theme.PmudemoTheme
import kotlinx.coroutines.launch

@Composable
fun CinemaEdit(
    navController: NavController, 
    viewModel: CinemaEditViewModel = viewModel(factory = AppViewModelProvider.Factory), 
    ) {
    val coroutineScope = rememberCoroutineScope()
    CinemaEdit(
        cinemaUiState = viewModel.cinemaUiState,
        onClick = {
            coroutineScope.launch {
                viewModel.saveCinema()
                navController.popBackStack()
            }
        },
        onUpdate = viewModel::updateUiState,
    )
}

@Composable
private fun CinemaEdit(
    cinemaUiState: CinemaUiState,
    onClick: () -> Unit,
    onUpdate: (CinemaDetails) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = cinemaUiState.cinemaDetails.name,
            onValueChange = { onUpdate(cinemaUiState.cinemaDetails.copy(name = it)) },
            label = { Text(stringResource(id = R.string.Cinema_name)) },
            singleLine = true
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = cinemaUiState.cinemaDetails.description,
            onValueChange = { onUpdate(cinemaUiState.cinemaDetails.copy(description = it)) },
            label = { Text(stringResource(id = R.string.Cinema_description)) },
            singleLine = true
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = cinemaUiState.cinemaDetails.year.toString(),
            onValueChange = { newValue ->
                val parsedYear = newValue.toLongOrNull() ?: 0L
                onUpdate(cinemaUiState.cinemaDetails.copy(year = parsedYear))
            },
            label = { Text(stringResource(id = R.string.Cinema_year)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = VisualTransformation.None // Отключает маскировку
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = cinemaUiState.cinemaDetails.image?.toString(Charsets.ISO_8859_1) ?: "",
            onValueChange = { newValue ->
                val byteArrayImage = newValue.toByteArray(Charsets.ISO_8859_1)
                onUpdate(cinemaUiState.cinemaDetails.copy(image = byteArrayImage))
            },
            label = { Text(stringResource(id = R.string.Cinema_image)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Button(
            onClick = onClick,
            enabled = cinemaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.Save_button))
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CinemaEditPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CinemaEdit(
                cinemaUiState = CinemaUiState(),
                onClick = {},
                onUpdate = {},
            )
        }
    }
}
