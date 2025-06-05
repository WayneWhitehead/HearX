package za.co.hidesign.hearx.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import za.co.hidesign.hearx.R

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("test") },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(4.dp, colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.start_test),
                        style = typography.displaySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate("results") },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(4.dp, colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.results),
                        style = typography.displaySmall
                    )
                }
            }
        }
    }
}