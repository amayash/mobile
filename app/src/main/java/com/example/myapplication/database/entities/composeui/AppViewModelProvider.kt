package com.example.myapplication.database.entities.composeui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.CinemaApplication
import com.example.myapplication.database.entities.composeui.edit.CinemaEditViewModel
import com.example.myapplication.database.entities.composeui.edit.SessionEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CinemaListViewModel(cinemaApplication().container.cinemaRestRepository)
        }
        initializer {
            CinemaEditViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.cinemaRestRepository
            )
        }
        initializer {
            CinemaViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.cinemaRestRepository,
            )
        }
        initializer {
            SessionListViewModel(
                cinemaApplication().container.sessionRestRepository,
                cinemaApplication().container.userSessionRestRepository,
            )
        }
        initializer {
            SessionEditViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.sessionRestRepository,
            )
        }
        initializer {
            CartViewModel(
                cinemaApplication().container.userSessionRestRepository,
                cinemaApplication().container.orderRestRepository,
                cinemaApplication().container.orderSessionRestRepository,
                cinemaApplication().container.userRestRepository,
            )
        }
        initializer {
            OrderListViewModel(
                cinemaApplication().container.orderRestRepository,
            )
        }
        initializer {
            OrderViewModel(
                this.createSavedStateHandle(),
                cinemaApplication().container.orderRestRepository,
            )
        }
    }
}

fun CreationExtras.cinemaApplication(): CinemaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CinemaApplication)