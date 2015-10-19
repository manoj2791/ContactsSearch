package com.example.manoj.contactssearch;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    ListView searchList,contactsList;
    SearchView searchView;
    TextView attend;
    Button addUser;
    public ArrayList<HashMap<String,String>> allUsersList;
    public ArrayList<HashMap<String,String>> addedUsersList;
    AttendeesAdapter attendeesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(51, 102, 204)));
        addUser=(Button)findViewById(R.id.addUser);
        searchView=(SearchView)findViewById(R.id.searchView1);
        contactsList=(ListView)findViewById(R.id.contactsView);
        searchList=(ListView)findViewById(R.id.searchViewList);
        attend=(TextView)findViewById(R.id.textView4);
        contactsList.setAdapter(attendeesAdapter);
        searchList.setAdapter(attendeesAdapter);
        allUsersList=new ArrayList<HashMap<String, String>>();
        addedUsersList=new ArrayList<HashMap<String, String>>();
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        if(i.hasExtra("attendees"))
        {
            addedUsersList=(ArrayList<HashMap<String,String>>)extras.getSerializable("attendees");
            contactsList.setAdapter(new AttendeesAdapter(MainActivity.this, addedUsersList,Boolean.FALSE));
        }
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    searchView.onActionViewCollapsed();
                }
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String searchString) {
                // TODO Auto-generated method stub
                if (searchString.length() > 0) {
                    contactSearch(searchString);
                    searchList.setAdapter(new AttendeesAdapter(MainActivity.this, allUsersList,Boolean.TRUE));
                }
//                else {
//                    searchList.setVisibility(View.GONE);
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchString) {

                if (searchString.length() > 1) {
                    contactSearch(searchString);
                    searchList.setAdapter(new AttendeesAdapter(MainActivity.this, allUsersList,Boolean.TRUE));
                }
//                  else {
//                    searchList.setVisibility(View.GONE);
//                }
                return false;
            }

        });
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                Log.e("Contact selected", "=========== Click");
                if (addedUsersList.contains(allUsersList.get(position)))
                    Toast.makeText(MainActivity.this, "Contact is already added", Toast.LENGTH_SHORT).show();
                else
                    addedUsersList.add(allUsersList.get(position));
                searchList.setVisibility(View.GONE);
                contactsList.setVisibility(View.VISIBLE);
                attend.setVisibility(View.VISIBLE);
                contactsList.setAdapter(new AttendeesAdapter(MainActivity.this, addedUsersList,Boolean.FALSE));
            }
        });
        if(addedUsersList.size()==0)
            attend.setVisibility(View.INVISIBLE);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=searchView.getQuery().toString();
                attend.setVisibility(View.VISIBLE);
                if(checkIfEmail(email))
                {
                    String userName = getUsernameFromEmail(email);
                    HashMap<String,String> contact=getdetails(userName, email);
                    if(addedUsersList.contains(contact))
                        Toast.makeText(MainActivity.this,"User already added",Toast.LENGTH_SHORT).show();
                    else {
                        addedUsersList.add(getdetails(userName, email));
                        contactsList.setAdapter(new AttendeesAdapter(MainActivity.this, addedUsersList,Boolean.FALSE));
                    }
                }
                else
                    Toast.makeText(MainActivity.this,"Type a valid Email address",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getUsernameFromEmail(String email)
    {
        if(email.contains("@"))
            return email.split("@")[0];
        return email;
    }

    public static Boolean checkIfEmail(String email)
    {
        if(email.contains("@")&&email.contains("."))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Override
    public void onBackPressed() {
    }

    public void contactSearch(String name)
    {
        searchList.setVisibility(View.VISIBLE);
        allUsersList.clear();
        name=name.toLowerCase();
        ContentResolver resolver = getContentResolver();

        Cursor c = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.HAS_PHONE_NUMBER + "!=0 AND (" + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?)",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
                ContactsContract.Data.CONTACT_ID);

        while (c.moveToNext()) {
            if(c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)).toLowerCase().contains(name)||c.getString(c.getColumnIndex(ContactsContract.Data.DATA1)).contains(name)) {
                allUsersList.add(getdetails(c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)),c.getString(c.getColumnIndex(ContactsContract.Data.DATA1))));
            }
        }
        c.close();
    }
    public HashMap<String,String> getdetails(String name,String email)
    {
        HashMap<String, String> contactDetail = new HashMap<String, String>();
        contactDetail.put("name", name);
        contactDetail.put("email", email);
        return contactDetail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==android.R.id.home)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
