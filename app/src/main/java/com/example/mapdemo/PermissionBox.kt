package com.example.mapdemo

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBox(
    modifier: Modifier = Modifier,
    description: String? = null,
    permissions: List<String>,
    requiredPermissions: List<String> = permissions,
    contentAlignment: Alignment = Alignment.TopStart,
    onGranted: @Composable BoxScope.(List<String>) -> Unit
) {
    val context = LocalContext.current
    var errorText by remember { mutableStateOf("") }

    val permissionState = rememberMultiplePermissionsState(permissions) { map ->
        val rejectedPermissions = map.filterValues { !it }.keys

        errorText = if (rejectedPermissions.none() { it in rejectedPermissions }) {
            ""
        } else {
            "${rejectedPermissions.joinToString()} required for the sample"
        }
    }

    val allRequiredPermissionGranted =
        permissionState.revokedPermissions.none() { it.permission in requiredPermissions }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = if (allRequiredPermissionGranted) {
            contentAlignment
        } else Alignment.Center
    ) {
        if (allRequiredPermissionGranted) {
            onGranted(permissionState.permissions.filter { it.status.isGranted }
                .map { it.permission })
        } else {
            PermissionScreen(
                permissionState
            )
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionScreen(
    state: MultiplePermissionsState
) {

    var showRationale by remember(state) {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.shouldShowRationale) {
        if (state.shouldShowRationale) {
            showRationale = true
        } else {
            state.launchMultiplePermissionRequest()
        }
    }


}