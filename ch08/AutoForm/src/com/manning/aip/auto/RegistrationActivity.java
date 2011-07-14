package com.manning.aip.auto;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
    TextView first;
    TextView last;
    TextView email;
    AutoCompleteTextView phone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        first = (TextView)findViewById(R.id.entry_first_name);
        last = (TextView)findViewById(R.id.entry_last_name);
        email = (TextView)findViewById(R.id.entry_email);
        phone = (AutoCompleteTextView)findViewById(R.id.entry_phone);
        
        phone.addTextChangedListener(new TextWatcher(){

            @Override
            public void afterTextChanged(Editable textInput) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (s.length() == 2){
                    PhoneLookupTask task = new PhoneLookupTask();
                    task.execute(s.toString());
                }
            }
            
        });

        email.setOnEditorActionListener(new OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
                submitForm();
                return true;
            }
        });
        
        Button doneButton = (Button)this.findViewById(R.id.done);
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }
    
    protected void submitForm() {
        InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromInputMethod(first.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        Contact contact = new Contact();
        contact.firstName=first.getText().toString();
        contact.lastName = last.getText().toString();
        contact.email = email.getText().toString();
        contact.phone = phone.getText().toString();
        Toast.makeText(this, 
                        "Contact submitted: " + contact, 
                        Toast.LENGTH_LONG)
                        .show();
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
            }
         }, 5000L);
    }

    static String removeFormat(String phone){
        if (phone == null){
            return null;
        }
        StringBuffer num = new StringBuffer();
        for (char ch : phone.toCharArray()){
            if (Character.isDigit(ch)){
                num.append(ch);
            }
        }
        return num.toString();
    }
    
    class PhoneLookupTask extends AsyncTask<String, Void, ArrayList<Contact>>{
        Context context = RegistrationActivity.this;
        @Override
        protected ArrayList<Contact> doInBackground(String... args) {
            ContactManager mgr = new ContactManager(context.getContentResolver());
            if (args.length == 0){
                return mgr.findByPhoneSubString(null);
            } 
            return mgr.findByPhoneSubString(args[0]);
        }
        
        @Override
        protected void onPostExecute(ArrayList<Contact> result){
            final HashMap<String, Contact> phoneMap = new HashMap<String, Contact>();
            final String[] phonesList = new String[result.size()];
            for (int i=0;i<result.size();i++){
                Contact oc = result.get(i);
                phonesList[i] = removeFormat(oc.phone);
                phoneMap.put(removeFormat(oc.phone), oc);
            }
            ArrayAdapter<String> phoneAdapter = 
                new ArrayAdapter<String>(context, R.layout.phone_suggest, phonesList);
            phone.setAdapter(phoneAdapter);
            phone.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> autoCompleteView,
                        View view, int position, long id) {
                    String phoneNum = (String) autoCompleteView
                            .getItemAtPosition(position);
                    Contact contact = phoneMap.get(phoneNum);
                    if (contact != null){
                        ContactDetailLookupTask task = new ContactDetailLookupTask();
                        task.execute(contact);
                    }
                }
            });
        }
    }
    class ContactDetailLookupTask extends AsyncTask<Contact, Void, Contact>{

        @Override
        protected Contact doInBackground(Contact... args) {
            ContactManager mgr = new ContactManager(getContentResolver());
            return mgr.getContact(args[0]);
        }

        @Override
        protected void onPostExecute(Contact contact) {
            if (contact == null){
                return;
            }
            if (!TextUtils.isEmpty(contact.firstName)) {
                first.setText(contact.firstName);
            } else {
                first.setText("");
            }
            if (!TextUtils.isEmpty(contact.lastName)) {
                last.setText(contact.lastName);
            } else {
                last.setText("");
            }
            if (!TextUtils.isEmpty(contact.email)) {
                email.setText(contact.email);
            } else {
                email.setText("");
            }
        }
        
    }
}