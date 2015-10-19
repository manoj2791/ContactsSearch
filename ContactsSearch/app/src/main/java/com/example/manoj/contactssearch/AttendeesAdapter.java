package com.example.manoj.contactssearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AttendeesAdapter extends BaseAdapter {

    Context mContext;
    int count;
    Button delete;
    Boolean isSeatchedUser;
    ArrayList<HashMap<String,String>> users;
    LayoutInflater inflator;
    public AttendeesAdapter(Activity activity, ArrayList<HashMap<String,String>> list,Boolean isSearched) {
        super();
        mContext=activity;
        inflator= LayoutInflater.from(this.mContext);
        this.count=list.size();
        this.isSeatchedUser=isSearched;
        this.users=list;
    }

    public AttendeesAdapter()
    {

    }
    @Override
    public int getCount() {
        return this.count;
    }
    @Override
    public HashMap<String,String> getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void replaceWith(Collection<HashMap<String,String>> newUsers) {
    this.users.clear();
    this.users.addAll(users);
    notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final UsersHolder searchedUsers;
        if(convertView ==null){
            convertView=inflator.inflate(R.layout.activity_attendees_adapter,null);
            searchedUsers=new UsersHolder();
        }
        else
        {
            searchedUsers=(UsersHolder)convertView.getTag();

        }
        searchedUsers.name=detail(convertView, R.id.name, users.get(position).get("name"));
        searchedUsers.email=detail(convertView, R.id.email, users.get(position).get("email"));
        if(!this.isSeatchedUser) {
            searchedUsers.deleteBtn = detail(convertView, R.id.deleteButton);
            searchedUsers.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    users.remove(position);
                    count = users.size();
                    notifyDataSetChanged();
                }
            });
        }
        else
        {
            searchedUsers.deleteBtn = detail(convertView, R.id.deleteButton);
            searchedUsers.deleteBtn.setVisibility(View.GONE);
        }
        convertView.setTag(searchedUsers);
        return convertView;
    }

    private TextView detail(View v, int resid, String text)
    {
        TextView tv=(TextView)v.findViewById(resid);
        tv.setText(text);
        return tv;
    }
    private Button detail(View v, int resid)
    {
        Button Btn=(Button)v.findViewById(resid);
        return Btn;
    }

    private static class UsersHolder{
        TextView name,email;
        Button deleteBtn;
    }
}
