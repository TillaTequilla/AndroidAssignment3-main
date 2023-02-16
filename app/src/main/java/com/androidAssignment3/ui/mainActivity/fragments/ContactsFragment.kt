package com.androidAssignment3.ui.mainActivity.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidAssignment3.R
import com.androidAssignment3.ui.mainActivity.adapter.ContactClickListener
import com.androidAssignment3.ui.mainActivity.adapter.ContactsRecycleViewAdapter
import com.androidAssignment3.architecture.BaseFragment
import com.androidAssignment3.util.Constance.ADD_CONTACT_RESULT_KEY
import com.androidAssignment3.model.Contact
import com.androidAssignment3.util.Constance
import com.androidAssignment3.util.SwipeToDeleteCallback
import com.androidAssignment3.databinding.FragmentContactsBinding
import com.google.android.material.snackbar.Snackbar


class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val contactViewModel: ContactsViewModel by activityViewModels()

    private val adapter: ContactsRecycleViewAdapter by lazy {
        ContactsRecycleViewAdapter(contactClickListener = object : ContactClickListener {
            override fun deleteUser(contact: Contact) {
                contactViewModel.deleteContact(contact)
                undoUserDeletion(binding.root, contact)
            }

            override fun showContact(contact: Contact) {
                val dialog = DialogFragmentShowContact()
                val args = Bundle()
                args.putSerializable(Constance.CONTACT_SERIALIZABLE, contact)
                dialog.arguments = args
                dialog.show(parentFragmentManager, "showContact")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setClickListeners()
        setFragmentListener()
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteUser(viewHolder.absoluteAdapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        contactViewModel.contactList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setClickListeners() {
        binding.ivContactBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvAddContact.setOnClickListener {
            val dialog = DialogFragmentAddContact()
            dialog.show(parentFragmentManager, "addContact")
        }
    }

    private fun setFragmentListener() {
        setFragmentResultListener(ADD_CONTACT_RESULT_KEY) { _, bundle ->
            val result = bundle.getSerializable(Constance.CONTACT_SERIALIZABLE)
            addContact(result as Contact)
        }
    }

    private fun addContact(contact: Contact) {
        contactViewModel.addContact(contact)
    }

    fun deleteUser(index: Int) {
        val contact = contactViewModel.contactList.value?.get(index)!!
        contactViewModel.deleteContact(index)
        undoUserDeletion(binding.root, contact)
    }


    private fun undoUserDeletion(view: View, contact: Contact?) {
        val delMessage = Snackbar.make(
            view,
            getString(R.string.contacts_sbRemoved, contact!!.name),
            Snackbar.LENGTH_LONG
        )
        delMessage.setAction("Cancel") {
            contactViewModel.addContact(contact)
        }.show()
    }

}