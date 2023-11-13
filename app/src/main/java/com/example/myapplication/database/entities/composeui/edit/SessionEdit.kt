package com.example.myapplication.database.entities.composeui.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.database.entities.composeui.AppViewModelProvider
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset


@Composable
fun SessionEdit(
    navController: NavController,
    viewModel: SessionEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    SessionEdit(
        sessionUiState = viewModel.sessionUiState,
        onClick = {
            coroutineScope.launch {
                viewModel.saveSession()
                navController.popBackStack()
            }
        },
        onUpdate = viewModel::updateUiState
    )
}

fun Long.toLocalDate(): org.threeten.bp.LocalDate {
    val instant = Instant.ofEpochMilli(this)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionEdit(
    sessionUiState: SessionUiState,
    onClick: () -> Unit,
    onUpdate: (SessionDetails) -> Unit,
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        item {
            if (sessionUiState.sessionDetails.dateTime != LocalDateTime.MIN) {
                val selectedDateMillis =
                    sessionUiState.sessionDetails.dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

                val dateState = rememberDatePickerState(
                    initialDisplayMode = DisplayMode.Input,
                    initialSelectedDateMillis = selectedDateMillis
                )
                val timeState = rememberTimePickerState(
                    sessionUiState.sessionDetails.dateTime.hour,
                    sessionUiState.sessionDetails.dateTime.minute
                )
                DatePicker(
                    state = dateState,
                    modifier = Modifier.padding(16.dp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timeState)
                }
                val selectedDate = dateState.selectedDateMillis?.toLocalDate()
                val selectedTime = LocalTime.of(timeState.hour, timeState.minute)
                if (selectedDate != null) {
                    val resultDateTime = LocalDateTime.of(selectedDate, selectedTime)
                    onUpdate(sessionUiState.sessionDetails.copy(dateTime = resultDateTime))
                }
            } else {
                val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
                val timeState = rememberTimePickerState()
                DatePicker(
                    state = dateState,
                    modifier = Modifier.padding(16.dp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timeState)
                }
                val selectedDate = dateState.selectedDateMillis?.toLocalDate()
                val selectedTime = LocalTime.of(timeState.hour, timeState.minute)
                if (selectedDate != null) {
                    val resultDateTime = LocalDateTime.of(selectedDate, selectedTime)
                    onUpdate(sessionUiState.sessionDetails.copy(dateTime = resultDateTime))
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = sessionUiState.sessionDetails.price,
                label = { Text(text = "Цена") },
                onValueChange = {
                    onUpdate(sessionUiState.sessionDetails.copy(price = it))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = sessionUiState.sessionDetails.maxCount.toString(),
                onValueChange = { newValue ->
                    val parsedMaxCount = newValue.toIntOrNull() ?: 0 // Преобразование в Int
                    onUpdate(sessionUiState.sessionDetails.copy(maxCount = parsedMaxCount))
                },
                label = { Text(text = "Количество") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
}