package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.CinemaApplication
import com.example.myapplication.database.entities.composeui.edit.CinemaEditViewModel
import com.example.myapplication.database.entities.composeui.edit.SessionEdit
import com.example.myapplication.database.entities.composeui.edit.SessionEditViewModel

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
                cinemaApplication().container.userSessionRepository,
            )
        }
        initializer {
            SessionEditViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.sessionRepository,
            )
        }
        initializer {
            CartViewModel(
                cinemaApplication().container.userSessionRepository,
                cinemaApplication().container.orderRepository,
                cinemaApplication().container.orderSessionRepository,
                cinemaApplication().container.userRepository,
            )
        }
    }
}

fun CreationExtras.cinemaApplication(): CinemaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CinemaApplication)