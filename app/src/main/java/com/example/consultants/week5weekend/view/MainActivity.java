package com.example.consultants.week5weekend.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.consultants.week5weekend.R;
import com.example.consultants.week5weekend.model.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnView)
    Button btnView;
    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;

    private static final int REQUEST_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessContact();
        ButterKnife.bind(this);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllContacts();
            }
        });
    }

    private void getAllContacts() {
        List<Contact> contactList = new ArrayList();
        Contact contact;

        //get contentresolver, cursor with query results
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    //set contact name
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contact = new Contact();
                    contact.setContactName(name);

                    //get and set phone number
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    //get and set email
                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA));
                        contact.setContactEmail(emailId);
                    }

                    emailCursor.close();

                    //get and set location
                    Cursor locationCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (locationCursor.moveToNext()) {
                        String location = locationCursor.getString(locationCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                        contact.setContactLocation(location);
                    }

                    locationCursor.close();
                    contactList.add(contact);
                }
            }

            //initialize fragment manager, pass it to adapter, to use in item's onclick
            FragmentManager fm = getSupportFragmentManager();

            RecyclerViewAdapter contactAdapter = new RecyclerViewAdapter(contactList, getApplicationContext(), fm);
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);
        }
    }

    //check permissions
    private void AccessContact() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
