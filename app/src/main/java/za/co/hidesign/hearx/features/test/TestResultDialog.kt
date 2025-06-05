import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import za.co.hidesign.hearx.R

@Composable
fun TestResultDialog(
    score: Int,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.test_complete)) },
        text = { Text(stringResource(R.string.your_score, score)) },
        confirmButton = {
            Button(onClick = onOkClick) { Text(stringResource(R.string.ok)) }
        }
    )
}