package com.example.run.presentation.di

import com.example.run.domain.RunningTracker
import com.example.run.presentation.active_run.ActiveRunViewmodel
import com.example.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewmodel)

    singleOf(::RunningTracker)
}