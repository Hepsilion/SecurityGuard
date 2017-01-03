package org.android.securityguard.black.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.black.db.dao.BlackNumberDao;
import org.android.securityguard.black.entity.BlackContactInfo;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class BlackContactAdapter extends BaseAdapter {
    private List<BlackContactInfo> contacts;
    private Context context;
    private BlackNumberDao dao;
    private BlackContactCallback callback;

    public BlackContactAdapter(List<BlackContactInfo> contacts, Context context){
        super();
        this.contacts=contacts;
        this.context=context;
        this.dao=new BlackNumberDao(context);
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHoler holder=null;
        if(view==null){
            view=View.inflate(context, R.layout.item_list_blackcontact, null);
            holder=new ViewHoler();
            holder.mNameTV= (TextView) view.findViewById(R.id.tv_black_name);
            holder.mModeTV= (TextView) view.findViewById(R.id.tv_black_mode);
            holder.mContactImgV=view.findViewById(R.id.view_black_icon);
            holder.mDeleteImgV=view.findViewById(R.id.view_black_delete);
            view.setTag(holder);
        }else{
            holder= (ViewHoler) view.getTag();
        }
        holder.mNameTV.setText(contacts.get(position).contactName+"("+contacts.get(position).phoneNumber+")");
        holder.mNameTV.setTextColor(context.getResources().getColor(R.color.bright_purple));
        holder.mModeTV.setText(contacts.get(position).getModeString(contacts.get(position).mode));
        holder.mModeTV.setTextColor(context.getResources().getColor(R.color.bright_purple));
        holder.mContactImgV.setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mDeleteImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean delete=dao.delete(contacts.get(position));
                if(delete){
                    contacts.remove(contacts.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    if(dao.getTotalNumber()==0){
                        callback.dataSizeChanged();
                    }
                }else{
                    Toast.makeText(context, R.string.security_delete_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void setCallBack(BlackContactCallback callback){
        this.callback=callback;
    }

    static class ViewHoler{
        TextView mNameTV;
        TextView mModeTV;
        View mContactImgV;
        View mDeleteImgV;
    }

    public interface BlackContactCallback{
        void dataSizeChanged();
    }
}
