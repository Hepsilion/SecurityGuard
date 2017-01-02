package org.android.securityguard.safe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.safe.entity.ContactInfo;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class ContactAdapter extends BaseAdapter {
    private List<ContactInfo> contacts;
    private Context context;

    public ContactAdapter(List<ContactInfo> contacts, Context context) {
        super();
        this.contacts=contacts;
        this.context=context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(view==null){
            view=View.inflate(context, R.layout.item_list_contact_select, null);
            holder=new ViewHolder();
            holder.mNameTV= (TextView) view.findViewById(R.id.tv_name);
            holder.mPhoneTV= (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.mNameTV.setText(contacts.get(position).name);
        holder.mPhoneTV.setText(contacts.get(position).phone);
        return view;
    }

    class ViewHolder{
        TextView mNameTV;
        TextView mPhoneTV;
    }
}
