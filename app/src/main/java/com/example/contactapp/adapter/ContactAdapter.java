package com.example.contactapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Contact;
import com.example.contactapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>  {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    Context mContext;
    List<Contact> contactList;
   // List<String> contactListAll;

    public ContactAdapter(Context mContext, List<Contact> contactList) {
        this.mContext = mContext;
        this.contactList = contactList;
      //  this.contactListAll = new ArrayList<E>(contactList)

    }



    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false);
        //LayoutInflater inflater = LayoutInflater.from(mContext);
        //View view = inflater.inflate(R.layout.list_item, parent, false);
        ContactViewHolder viewHolder  = new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.name.setText(contact.getName());
       holder.number.setText(contact.getPhone());
        if (contact.getPhoto() != null){
            Picasso.get().load(contact.getPhoto()).into(holder.imageView);
        }else {
             holder.imageView.setImageResource(R.drawable.person);
        }
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String callnumber=holder.number.getText().toString();
                String callname=holder.name.getText().toString();
                Toast.makeText(context,"Calling :"
                        +callname+"("+callnumber+")", Toast.LENGTH_LONG).show();
                String action = Intent.ACTION_CALL;
                Intent callIntent = new Intent(action);
                callIntent.setData(Uri.parse("tel:"+callnumber));
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        context.startActivity(callIntent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return contactList.size();

    }
    public void setFilter(List<Contact> FilteredDataList) {
        contactList = FilteredDataList;
        notifyDataSetChanged();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView number;
        ImageButton callButton;
       // CardView cardView;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView =itemView.findViewById(R.id.imageView);
            this.name = itemView.findViewById(R.id.name);
            this.number = itemView.findViewById(R.id.number);
            this.callButton = itemView.findViewById(R.id.callButton);

        }
    }


}
