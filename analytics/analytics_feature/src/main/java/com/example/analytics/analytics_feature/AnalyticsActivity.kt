package com.example.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.analytics.data.di.analyticsDataModule
import com.example.analytics.presentation.AnalyticsDashboardScreenRoot
import com.example.analytics.presentation.di.analyticsPresentationModule
import com.example.core.presentation.designsystem.RunikTheme
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.core.context.loadKoinModules

class AnalyticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(
            listOf(
                analyticsDataModule,
                analyticsPresentationModule
            )
        )
        SplitCompat.installActivity(this)

        setContent {
            RunikTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "analytics_dashboard") {
                    composable("analytics_dashboard") {
                       AnalyticsDashboardScreenRoot(
                           onBackClick = { finish() }
                       )
                    }
                }
            }
        }
    }
}