package za.co.hidesign.hearx.features.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import za.co.hidesign.hearx.MainActivity.Companion.HOME
import za.co.hidesign.hearx.MainActivity.Companion.RESULT_DIALOG
import za.co.hidesign.hearx.R
import za.co.hidesign.hearx.features.test.TestViewModel.Companion.TOTAL_ROUNDS

@Composable
fun TestScreen(
    navController: NavController,
    viewModel: TestViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(state.round) {
        viewModel.onEvent(TestEvent.StartRound)
    }

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is TestNavEvent.NavigateHome -> navController.popBackStack(HOME, false)
            is TestNavEvent.DisplayResultsDialog -> navController.navigate("$RESULT_DIALOG/${state.score}")
            else -> {}
        }
    }

    Scaffold { padding ->
        Box(Modifier.fillMaxSize().padding(32.dp).padding(padding)) {
            if (state.showErrorDialog) {
                DisplayErrorDialog(
                    title = state?.errorTitle ?: stringResource(R.string.error_title),
                    description = state?.errorDescription ?: stringResource(R.string.error_message),
                    onTryAgain = { viewModel }
                )
            }

            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = stringResource(R.string.round, (state.round + 1).coerceAtMost(TOTAL_ROUNDS), TOTAL_ROUNDS),
                style = typography.headlineSmall
            )

            Box(Modifier.align(Alignment.Center)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = when {
                            state.isPlaying -> stringResource(R.string.playing_now)
                            state.isWaiting -> stringResource(R.string.prepare_to_listen)
                            state.round >= TOTAL_ROUNDS -> stringResource(R.string.submitting_test)
                            else -> stringResource(R.string.enter_what_you_heard)
                        },
                        style = typography.headlineSmall
                    )
                    AnimatedVisibility(state.isWaiting) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(64.dp))
                            if (state.countdown > 0) {
                                Text(
                                    text = state.countdown.toString(),
                                    style = typography.headlineMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!state.isWaiting && state.round < TOTAL_ROUNDS) {
                    DigitInputs(
                        state = state,
                        onInputChanged = { viewModel.onEvent(TestEvent.InputChanged(it)) }
                    )
                }

                Button(
                    onClick = { viewModel.onEvent(TestEvent.SubmitDigits) },
                    enabled = state.input.length == 3 && !state.isPlaying,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.submit),
                        style = typography.labelMedium
                    )
                }
                OutlinedButton(
                    onClick = { viewModel.onNavEvent(TestNavEvent.NavigateHome) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.exit_test),
                        style = typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun DigitInputs(
    state: TestUiState,
    onInputChanged: (String) -> Unit,
) {
    val focusRequesters = List(3) { remember { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    var digits by remember { mutableStateOf(state.input.padEnd(3, ' ')) }

    LaunchedEffect(state.input) {
        digits = state.input.padEnd(3, ' ')
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0..2) {
            TextField(
                value = digits.getOrNull(i)?.takeIf { it != ' ' }?.toString() ?: "",
                onValueChange = { new ->
                    if (new.length <= 1 && (new.isEmpty() || new[0].isDigit())) {
                        val chars = digits.toCharArray()
                        chars[i] = new.getOrNull(0) ?: ' '
                        digits = String(chars)
                        onInputChanged(digits.trim())
                        if (new.isNotEmpty() && i < 2) {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                        if (new.isEmpty() && i > 0) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        }
                    }
                    if (i == 2 && digits[i] != ' ') focusManager.clearFocus()
                },
                modifier = Modifier.weight(1f).focusRequester(focusRequesters[i]),
                singleLine = true,
                textStyle = typography.displayMedium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (i == 2) ImeAction.Done else ImeAction.Next
                ),
                maxLines = 1
            )
        }
    }
}