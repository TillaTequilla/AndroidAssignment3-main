package com.androidAssignment3.util

import androidx.recyclerview.widget.DiffUtil
import com.androidAssignment3.model.Contact

object DiffUtil : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return  oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}
