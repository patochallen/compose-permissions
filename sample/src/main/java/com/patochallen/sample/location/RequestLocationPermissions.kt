package com.patochallen.sample.location

import android.Manifest.permission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patochallen.permissions.model.ExperimentalApi
import com.patochallen.permissions.ui.RequestPermissions
import com.patochallen.sample.R

@OptIn(ExperimentalApi::class)
@Composable
fun RequestLocationPermissions(
    content: @Composable (() -> Unit)
) {
    RequestPermissions(
        permission = permission.ACCESS_COARSE_LOCATION,
        showRationalContent = { ShowRationalContent() },
        permissionDeniedContent = { PermissionDeniedContent() },
        content = content
    )
}

@Composable
fun ShowRationalContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .shadow(15.dp, CircleShape, false)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Enable Location",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Please provide access to\nyour location, which is required\nfor use the App",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            lineHeight = 25.sp
        )
    }
}

@Composable
fun PermissionDeniedContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .shadow(15.dp, CircleShape, false)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
            )
            Image(
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 25.unaryMinus().dp, y = 25.unaryMinus().dp)
                    .shadow(5.dp, CircleShape, false)
                    .background(MaterialTheme.colors.error, CircleShape)
                    .padding(15.dp)
                    .padding(bottom = 5.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onError)
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Enable Location",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "The permission has been denied\ntwo times, please open settings\nand grant permissions manually",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .border(2.dp, MaterialTheme.colors.error, RoundedCornerShape(16.dp))
                .background(
                    MaterialTheme.colors.error.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            lineHeight = 25.sp,
            color = MaterialTheme.colors.onSurface
        )
    }
}