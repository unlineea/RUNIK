package com.example.run.presentation.active_run

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.RunikTheme
import com.example.core.presentation.designsystem.StartIcon
import com.example.core.presentation.designsystem.StopIcon
import com.example.core.presentation.designsystem.components.RunikFloatingActionButton
import com.example.core.presentation.designsystem.components.RunikScaffold
import com.example.core.presentation.designsystem.components.RunikToolbar
import com.example.run.presentation.R
import com.example.run.presentation.active_run.components.RunDataCard
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.Modifier

@Composable

fun ActiveRunScreenRoot(

    viewModel: ActiveRunViewmodel = koinViewModel()

) {

    ActiveRunScreen(

        state = viewModel.state,

        onAction = viewModel::onAction

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

private fun ActiveRunScreen(

    state: ActiveRunState,

    onAction: (ActiveRunAction) -> Unit

) {
    RunikScaffold(
        withGradient = false,
        topAppBar = {
            RunikToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                },
            )
        },
        floatingActionButton = {
            RunikFloatingActionButton(
                icon = if (state.shouldTrack) {
                    StopIcon
                } else {
                    StartIcon
                },
                onClick = {
                    onAction(ActiveRunAction.OnToggleRunClick)
                },
                iconSize = 20.dp,
                contentDescription = if(state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview

@Composable

private fun ActiveRunScreenPreview() {

    RunikTheme {

        ActiveRunScreen(

            state = ActiveRunState(),

            onAction = {}

        )

    }

}