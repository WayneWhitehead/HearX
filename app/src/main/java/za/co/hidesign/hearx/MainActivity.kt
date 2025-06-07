package za.co.hidesign.hearx

import TestResultDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import za.co.hidesign.hearx.features.home.HomeScreen
import za.co.hidesign.hearx.features.results.ResultsScreen
import za.co.hidesign.hearx.features.test.TestScreen
import za.co.hidesign.hearx.ui.theme.HearXTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearXTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HOME) {
                    composable(HOME) { HomeScreen(navController) }
                    composable(TEST) { TestScreen(navController) }
                    composable(RESULTS) { ResultsScreen(navController) }
                    dialog("$RESULT_DIALOG/{score}") { backStackEntry ->
                        TestResultDialog(
                            score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0,
                            onDismiss = { navController.popBackStack("home", false) }
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val HOME = "home"
        const val TEST = "test"
        const val RESULTS = "results"
        const val RESULT_DIALOG = "result_dialog"
    }
}