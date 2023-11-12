package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.CinemaApplication
import com.example.myapplication.database.entities.composeui.edit.CinemaEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CinemaListViewModel(cinemaApplication().container.cinemaRepository)
        }
        initializer {
            CinemaEditViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.cinemaRepository
            )
        }
        initializer {
            CinemaViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.cinemaRepository,
                cinemaApplication().container.sessionRepository,
            )
        }
    }
}

fun CreationExtras.cinemaApplication(): CinemaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CinemaApplication)