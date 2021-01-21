package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactapp.adapter.ContactAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;


public class MainActivity extends AppCompatActivity {
    //  inputSearch = findViewById(R.id.inputSearch);
    List<Contact> contactList , filteredContactList;
    ContactAdapter adapter;
  //  android.widget.SearchView inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvContacts = findViewById(R.id.rvContacts);
        rvContacts.setHasFixedSize(true);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();

        adapter = new ContactAdapter(this,contactList);
        rvContacts.setAdapter(adapter);



        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (response.getPermissionName().equals(Manifest.permission.READ_CONTACTS)){
                            getContacts();
                          //  Intent i = new Intent(Intent.ACTION_CALL);
                            //i.setData(Uri.parse("tel:"+phoneNumber));
                            //startActivity(i);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "Permission should be granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

 /*   private void filter(String toString) {
        ArrayList<Contact> filterdNames = new ArrayList<>();

        for (Contact s : contactList) {

            if (s.toLowerCase().contains(toString.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        adapter.filterList(filterdNames);
        }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredContactList = filter(contactList, newText);
                adapter.setFilter(filteredContactList);
                return false;
            }

        });

        return true;
    }
//        return super.onCreateOptionsMenu(menu);

    private List<Contact> filter(List<Contact> contactList, String newText) {
        newText=newText.toLowerCase();
        String text;
        filteredContactList=new ArrayList<>();
        for(Contact dataFromDataList:contactList){
            text=dataFromDataList.getName().toLowerCase();

            if(text.contains(newText)){
                filteredContactList.add(dataFromDataList);
            }
        }

        return filteredContactList;

    }

    private void getContacts() {
        String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,order);
        String tname ="";
        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(tname))
                continue;
            tname=name;
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            Contact contact = new Contact(name,phoneNumber,phoneUri);
            contactList.add(contact);
          //  Collections.sort(contactList);
            adapter.notifyDataSetChanged();

        }
    }
}

