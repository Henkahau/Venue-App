package com.henkahau.venueapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.henkahau.venueapp.location.LocationUtils
import com.henkahau.venueapp.ui.components.MainLayout
import com.henkahau.venueapp.ui.theme.VenueAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            lifecycle.addObserver(viewModel)

            val context = LocalContext.current

            // Check if location permissions are already granted
            var permissionsGranted by remember {
                mutableStateOf(
                    LocationUtils.checkHasLocationPermission(context)
                )
            }
            val launcherPermissions = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                permissionsGranted = it.values.reduce { acc, next -> acc && next }
            }

            VenueAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   if(permissionsGranted) {
                       MainLayout(mainViewModel = viewModel)
                   } else {
                       RequestPermissionButton {
                           checkAndRequestLocationPermission(
                               context = context,
                               permissions = LocationUtils.locationPermissions,
                               launcher = launcherPermissions
                           )
                       }
                   }
                }
            }
        }
    }

    @Composable
    private fun RequestPermissionButton(onClick: () -> Unit) {
        Button(modifier = Modifier.wrapContentSize(), onClick = onClick) {
            Text(text = "Allow using your location to search nearby venues")
        }
    }

    private fun checkAndRequestLocationPermission(
        context: Context,
        permissions: Array<String>,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        ) {
        if (permissions.any {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            launcher.launch(permissions)
        }
    }
}