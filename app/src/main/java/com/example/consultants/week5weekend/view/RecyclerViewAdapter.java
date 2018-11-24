package com.example.consultants.week5weekend.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.consultants.week5weekend.R;
import com.example.consultants.week5weekend.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Contact> contactList;
    private Context context;
    private FragmentManager fm;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, FragmentManager fm) {
        this.contactList = contactList;
        this.context = context;
        this.fm = fm;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //create view from list item xml
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int i) {
        //set information into views
        Contact contact = contactList.get(i);
        holder.tvContactName.setText(contact.getContactName());
        holder.tvPhoneNumber.setText(contact.getContactNumber());

        //add onclicklistener to display detailed info fragment
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragment detailFragment = new DetailFragment();
                //bundle with data to send and be shown in fragment
                Bundle bundle = new Bundle();
                bundle.putString("name", contact.getContactName());
                bundle.putString("number", contact.getContactNumber());
                bundle.putString("email", contact.getContactEmail());
                bundle.putString("location", contact.getContactLocation());
                detailFragment.setArguments(bundle);
                detailFragment.show(fm, "DetailFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}
