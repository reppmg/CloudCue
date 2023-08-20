package repp.max.cloudcue.screens.weather_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import repp.max.cloudcue.screens.weather_list.permission.PermissionViewModel
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionEvent
import repp.max.cloudcue.screens.weather_list.permission.models.PermissionState
import repp.max.cloudcue.ui.theme.Dimen
import repp.max.cloudcue.ui.theme.TransparentWhite
import timber.log.Timber

@Composable
fun PermissionRationale(
    permissionViewModel: PermissionViewModel,
    viewModelPermissionState: PermissionState.Denied
) {
    Timber.d("PermissionNotion: ${viewModelPermissionState.shouldNavigateToSettings}")
    Card(
        colors = CardDefaults.cardColors(
            containerColor = TransparentWhite
        ),
        modifier = Modifier
            .padding(
                vertical = Dimen.baseVertical,
                horizontal = Dimen.baseHorizontal
            )
            .clickable { permissionViewModel.apply(PermissionEvent.OnPermissionRequested) }
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(
                vertical = Dimen.baseVertical,
                horizontal = Dimen.baseHorizontal
            )
        ) {
            Text(
                text = "This app requires location permission to show actual weather nearby",
                fontSize = 14.sp,
                lineHeight = 18.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = TransparentWhite
                ),
                modifier = Modifier
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp, horizontal = 6.dp
                        )
                ) {
                    Text(
                        text = if (viewModelPermissionState.shouldNavigateToSettings) {
                            "Grant permission"
                        } else {
                            "Go to settings"
                        },
                        fontSize = 16.sp
                    )
                }
            }
        }

    }
}

