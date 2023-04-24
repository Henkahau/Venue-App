package com.henkahau.venueapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.henkahau.venueapp.location.LocationUtils
import com.henkahau.venueapp.ui.components.Search
import com.henkahau.venueapp.ui.components.VenueList
import com.henkahau.venueapp.ui.theme.VenueAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
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
                    val mainViewModel: MainViewModel = viewModel()
                    if (permissionsGranted) {
                        VenueSearchLayout(viewModel = mainViewModel)
                    } else {
                        RequestPermissionButton {
                            checkAndRequestLocationPermission(
                                context,
                                LocationUtils.locationPermissions,
                                launcherPermissions
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun VenueSearchLayout(viewModel: MainViewModel) {
        val venues by viewModel.getVenues().collectAsState()

        Column {
            Search(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                onTextChanged = viewModel::searchVenues
            )
            VenueList(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                venues = venues
            )
        }
    }

    @Composable
    private fun RequestPermissionButton(onClick: () -> Unit) {
        Button(onClick = onClick) {
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