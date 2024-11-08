package com.fin.technicalapp.di

import com.fin.technicalapp.feature.home.HomeViewModel
import com.fin.technicalapp.feature.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}