package com.example.myapplication.database.entities.composeui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.database.entities.composeui.AppViewModelProvider
import com.example.myapplication.database.entities.model.Cinema
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun SessionEdit(
    navController: NavController,
    viewModel: SessionEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    cinemaViewModel: CinemaDropDownViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    cinemaViewModel.setCurrentCinema(viewModel.sessionUiState.sessionDetails.cinemaId)
    SessionEdit(
        sessionUiState = viewModel.sessionUiState,
        cinemaUiState = cinemaViewModel.cinemaUiState,
        cinemasListUiState = cinemaViewModel.cinemasListUiState,
        onClick = {
            coroutineScope.launch {
                viewModel.saveSession()
                navController.popBackStack()
            }
        },
        onUpdate = viewModel::updateUiState,
        onCinemaUpdate = cinemaViewModel::updateUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CinemaDropDown(
    cinemaUiState: LocalCinemaUiState,
    cinemasListUiState: CinemasListUiState,
    onCinemaUpdate: (Cinema) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(top = 7.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = cinemaUiState.cinema?.name
                ?: stringResource(id = R.string.session_cinema_not_select),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .exposedDropdownSize()
        ) {
            cinemasListUiState.cinemaList.forEach { cinema ->
                DropdownMenuItem(
                    text = {
                        Text(text = cinema.name)
                    },
                    onClick = {
                        onCinemaUpdate(cinema)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionEdit(
    sessionUiState: SessionUiState,
    cinemaUiState: LocalCinemaUiState,
    cinemasListUiState: CinemasListUiState,
    onClick: () -> Unit,
    onUpdate: (SessionDetails) -> Unit,
    onCinemaUpdate: (Cinema) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        var pickedDateTime by remember {
            mutableStateOf(LocalDateTime.now())
        }

        val formattedDate by remember {
            derivedStateOf {
                DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm")
                    .format(pickedDateTime)
            }
        }

        val dateDialogState = rememberMaterialDialogState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                dateDialogState.show()
            }) {
                Text(text = formattedDate)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        CinemaDropDown(
            cinemaUiState = cinemaUiState,
            cinemasListUiState = cinemasListUiState,
            onCinemaUpdate = {
                onUpdate(sessionUiState.sessionDetails.copy(cinemaId = it.uid))
                onCinemaUpdate(it)
            }
        )
        Button(
            onClick = onClick,
            enabled = sessionUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.Save_button))
        }
    }
}