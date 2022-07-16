package com.patochallen.sample.screens.contacts

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.patochallen.permissions.model.ExperimentalPermissionApi
import com.patochallen.sample.screens.contacts.model.Contact
import com.patochallen.sample.utils.launchContactIntent

@OptIn(ExperimentalPermissionApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen() = RequestContactsPermission {
    val context = LocalContext.current
    val viewModel = viewModel<ContactListViewModel>()
    val contactList by viewModel.contactList.collectAsState(initial = emptyMap())

    if (contactList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            state = rememberLazyListState()
        ) {
            contactList.forEach { (letter, contactsForLetter) ->
                stickyHeader {
                    ContactHeader(letter)
                }

                items(
                    items = contactsForLetter,
                    key = { it.id }
                ) { contact ->
                    ContactItem(contact) {
                        context.launchContactIntent(contact.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactHeader(letter: String) {
    Text(
        text = letter,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(horizontal = 15.dp, vertical = 5.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ContactItem(
    contact: Contact,
    onClick: () -> Unit
) {
    val starColor = if (contact.starred) Color(0xFFFF9800) else Color(0xFFDDDDDD)
    Card(onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactCircleImage(
                uri = contact.photoUri,
                letter = contact.name.first().uppercase(),
                backgroundColor = contact.color
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = contact.name,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis
            )
            if (contact.hasPhone) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "phone icon",
                    tint = Color.DarkGray
                )
            }
            if (contact.hasEmail) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "email icon",
                    tint = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "starred icon",
                tint = starColor
            )
        }
    }
}

@Composable
private fun ContactCircleImage(
    uri: Uri,
    letter: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(backgroundColor, CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (uri.path.orEmpty().isEmpty()) {
            Text(
                text = letter,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "contact image",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
