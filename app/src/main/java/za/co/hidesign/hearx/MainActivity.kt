package za.co.hidesign.hearx

import TestResultDialog
import TestScreen
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
import za.co.hidesign.hearx.ui.theme.HearXTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HearXTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("test") { TestScreen(navController) }
                    composable("results") { ResultsScreen(navController) }
                    dialog("result_dialog/{score}") { backStackEntry ->
                        TestResultDialog(
                            score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0,
                            onDismiss = { navController.popBackStack("home", false) }
                        )
                    }
                }
            }
        }
    }
}