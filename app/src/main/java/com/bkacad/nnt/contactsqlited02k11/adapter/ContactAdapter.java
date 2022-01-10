package com.bkacad.nnt.contactsqlited02k11.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bkacad.nnt.contactsqlited02k11.R;
import com.bkacad.nnt.contactsqlited02k11.model.Contact;

import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        }
        // Bind id
        TextView tvName = convertView.findViewById(R.id.tv_item_contact_name);
        TextView tvPhone = convertView.findViewById(R.id.tv_item_contact_phone);

        // Đổ dữ liệu vào view
        Contact item = contactList.get(position);
        tvName.setText(item.getName());
        tvPhone.setText(item.getPhone());

        return convertView;
    }
}
