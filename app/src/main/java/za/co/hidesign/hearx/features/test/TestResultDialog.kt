import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import za.co.hidesign.hearx.R

@Composable
fun TestResultDialog(
    score: Int,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.test_complete)) },
        text = { Text(stringResource(R.string.your_score, score)) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.ok)) }
        }
    )
}