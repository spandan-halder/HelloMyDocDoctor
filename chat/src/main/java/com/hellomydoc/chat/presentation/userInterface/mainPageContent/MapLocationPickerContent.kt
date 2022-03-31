package com.hellomydoc.chat.presentation.userInterface.mainPageContent

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.hellomydoc.chat.ChatViewModel
import com.hellomydoc.chat.presentation.Style

@Composable
fun MapLocationPickerContent(viewModel: ChatViewModel) {
    if(viewModel.locationPicking.value){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Style.mapLocationPickerBackground)
        ){
            LocationPickerContent(viewModel)
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPickerContent(viewModel: ChatViewModel) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    if (locationPermissionState.allPermissionsGranted) {
        MapPickerContent(viewModel)
    }
    else{
        MapPermissionAllowContent(viewModel,locationPermissionState)
    }
}

@Composable
private fun MapPickerContent(viewModel: ChatViewModel) {
    GoogleMapContent(viewModel)
    MapDoneButton(viewModel)
}

@Composable
private fun MapDoneButton(viewModel: ChatViewModel) {
    Button(
        modifier = Modifier.padding(Style.mapPermissionAlloweButtonPadding.dp),
        onClick = {
            viewModel.onLocationPicked()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Style.mapButtonBackgroundColor,
            contentColor = Style.mapButtonContentColor
        )
    ) {
        Text(Style.mapDoneText)
    }
}


@Composable
private fun GoogleMapContent(viewModel: ChatViewModel) {
    var properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = true
            )
        )
    }

    GoogleMap(
        properties = properties,
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(),
        onMapLongClick = {

        },
        onMapClick = {
            viewModel.onMarker(it)
        }
    ) {
        MarkerContent(viewModel)
    }
}

@Composable
private fun MarkerContent(viewModel: ChatViewModel) {
    val latLng = viewModel.attachment.value.latLng?:return
    Marker(
        position = LatLng(latLng.latitude, latLng.longitude),
        icon = BitmapDescriptorFactory.defaultMarker(
            BitmapDescriptorFactory.HUE_RED
        ),
        title = "${latLng.latitude},${latLng.longitude}",
        snippet = "Selected Location",
        onClick = {
            it.showInfoWindow()
            it.isVisible
        },
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapPermissionAllowContent(
    viewModel: ChatViewModel,
    locationPermissionState: MultiplePermissionsState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        MapPermissionMessage()
        MapPermissionAllowButton(locationPermissionState)
    }
}

@Composable
private fun MapPermissionMessage() {
    Text(
        Style.mapPermissionAllowMessage,
        modifier = Modifier.padding(
            Style.mapPermissionAllowMessagePadding.dp
        )
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapPermissionAllowButton(locationPermissionState: MultiplePermissionsState) {
    Button(
        onClick = {
            locationPermissionState.launchMultiplePermissionRequest()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Style.mapButtonBackgroundColor,
            contentColor = Style.mapButtonContentColor
        ),
        modifier = Modifier.padding(bottom = Style.mapPermissionAllowButtonBottomPadding.dp)
    ) {
        Text(
            Style.mapPermissionAllowButtonText,
        )
    }
}