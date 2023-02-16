package com.androidAssignment3.ui.mainActivity.adapter

import com.androidAssignment3.model.Contact

interface ContactClickListener {
    fun deleteUser(contact: Contact)
    fun showContact(contact: Contact)
}