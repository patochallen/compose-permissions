package com.patochallen.sample.screens.contacts

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.Contacts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.patochallen.sample.screens.contacts.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val resolver: ContentResolver
) : ViewModel() {

    val contactList = flow {
        emit(getContacts())
    }.map { list ->
        list.groupBy { it.name.first().uppercase() }
    }

    private fun getContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        resolver.query(
            /* uri = */ Contacts.CONTENT_URI,
            /* projection = */ null,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ Contacts.DISPLAY_NAME + SORT_ORDER
        )?.run {
            while (count > 0 && moveToNext()) {
                getContact()?.let {
                    contacts.add(it)
                }
            }
            close()
        }
        return contacts
    }

    @Suppress("Range", "TooGenericExceptionCaught", "PrintStackTrace")
    private fun Cursor.getContact(): Contact? = try {
        val id = getString(getColumnIndex(Contacts._ID)).orEmpty()
        val name = getString(getColumnIndex(Contacts.DISPLAY_NAME)).orEmpty()
        val photoUri = getString(getColumnIndex(Contacts.PHOTO_URI)).orEmpty().toUri()
        val starred = getInt(getColumnIndex(Contacts.STARRED)) == 1
        val hasPhone = getInt(getColumnIndex(Contacts.HAS_PHONE_NUMBER)) == 1
        val hasEmail = contactHasEmail(id)
        Contact(
            id = id,
            name = name,
            starred = starred,
            photoUri = photoUri,
            hasPhone = hasPhone,
            hasEmail = hasEmail
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    @Suppress("TooGenericExceptionCaught", "PrintStackTrace")
    private fun contactHasEmail(contactId: String): Boolean = try {
        resolver.query(
            /* uri = */ Email.CONTENT_URI,
            /* projection = */ null,
            /* selection = */ Email.CONTACT_ID + SQL_MARKER,
            /* selectionArgs = */ arrayOf(contactId),
            /* sortOrder = */ null
        )?.run {
            val hasEmail = count > 0
            close()
            hasEmail
        } ?: false
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    private companion object {
        const val SORT_ORDER = " ASC"
        const val SQL_MARKER = " = ?"
    }
}
