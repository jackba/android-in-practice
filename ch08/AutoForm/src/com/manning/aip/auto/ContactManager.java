package com.manning.aip.auto;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

public class ContactManager {

    private final ContentResolver resolver;
    
    public ContactManager(ContentResolver cr){
        resolver = cr;
    }
    
    public Contact getContact(Contact partial){
        Contact contact = new Contact();
        contact.id = partial.id;
        contact.phone = partial.phone;
        String[] projection = new String[] {StructuredName.GIVEN_NAME, 
                                            StructuredName.FAMILY_NAME, 
                                            StructuredName.RAW_CONTACT_ID, 
                                            StructuredName.CONTACT_ID};
        String selection = StructuredName.CONTACT_ID+ " = ? AND " + 
            Data.MIMETYPE + " = '" + StructuredName.CONTENT_ITEM_TYPE +"'";
        String[] selectionArgs = new String[] {contact.id};
        Cursor nameCursor = null;
        try{
            nameCursor = resolver.query(Data.CONTENT_URI, 
                                        projection, 
                                        selection, 
                                        selectionArgs, 
                                        null);
            if (nameCursor.moveToFirst()){
                contact.firstName = nameCursor.getString(
                        nameCursor.getColumnIndex(StructuredName.GIVEN_NAME));
                contact.lastName = nameCursor.getString(
                        nameCursor.getColumnIndex(StructuredName.FAMILY_NAME));
                
            }
        } finally {
            if (nameCursor != null) nameCursor.close();
        }
        projection = new String[] {Email.DATA1, Email.CONTACT_ID};
        selection = Email.CONTACT_ID + " = ?";
        Cursor emailCursor = null;
        try{
            emailCursor = resolver.query(Email.CONTENT_URI, 
                                         null, 
                                         selection, 
                                         selectionArgs, 
                                         null);
            if (emailCursor.moveToFirst()){
                contact.email = emailCursor.getString(
                        emailCursor.getColumnIndex(Email.DATA1));
            }
        } finally{
            if (emailCursor != null) emailCursor.close();
        }
        return contact;
    }

    public ArrayList<Contact> findByPhoneSubString(String phoneSubStr){
        String[] projection = {Phone.CONTACT_ID, Phone.NUMBER};
        String selection = Data.IN_VISIBLE_GROUP + "=1 AND " + 
            Phone.NUMBER + " LIKE ?";
        String[] selectionArgs = {"%" + phoneSubStr + "%"};
        if (phoneSubStr == null){
            selection = null;
            selectionArgs = null;
        }
        Cursor phoneCursor = null;
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try{
            phoneCursor = resolver.query(Phone.CONTENT_URI, 
                                         projection, 
                                         selection, 
                                         selectionArgs, 
                                         null);
            int idCol = phoneCursor.getColumnIndex(Phone.CONTACT_ID);
            int numCol = phoneCursor.getColumnIndex(Phone.NUMBER);
            while (phoneCursor.moveToNext()){
                long id = phoneCursor.getLong(idCol);
                String phoneNum = phoneCursor.getString(numCol);
                Contact contact = new Contact();
                contact.phone = phoneNum;
                contact.id = String.valueOf(id);
                contacts.add(contact);
            }
        } finally {
            if (phoneCursor != null) phoneCursor.close();
        }
        return contacts;
    }
}
