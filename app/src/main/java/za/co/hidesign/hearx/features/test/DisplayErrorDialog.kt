package za.co.hidesign.hearx.features.test

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import za.co.hidesign.hearx.R

@Composable
fun DisplayErrorDialog(
    title: String,
    description: String,
    onTryAgain: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = { Text(description) },
        confirmButton = {
            Button(
                onClick = onTryAgain,
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.try_again)) }
        }
    )
}