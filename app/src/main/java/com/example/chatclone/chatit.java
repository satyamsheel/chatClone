package com.example.chatclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class chatit extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Toolbar toolbar;
    ArrayList<String>  appuser;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView=findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        appuser=new ArrayList<>();
        final ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,appuser);
        final SwipeRefreshLayout swiperefresh=findViewById(R.id.swiperefresh);

        try
        {
            ParseQuery<ParseUser> parseQuery =ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseUser user :objects)
                        {
                            appuser.add(user.getUsername());
                        }
                        listView.setAdapter(adapter);
                    }
                }
            });
        }catch (Exception e)
        {e.printStackTrace();}


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    ParseQuery<ParseUser> parseQuery =ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",appuser);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(objects.size()>0){
                                if(e==null){
                                    for(ParseUser user :objects)
                                    {appuser.add(user.getUsername());}
                                    adapter.notifyDataSetChanged();
                                    if(swiperefresh.isRefreshing()){
                                        swiperefresh.setRefreshing(false);
                                    }
                                }
                            }else {
                                if (swiperefresh.isRefreshing())
                                {swiperefresh.setRefreshing(false);}
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });





    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent =new Intent(chatit.this,finalchat.class);
        intent.putExtra("selectedUser",appuser.get(position));
        startActivity(intent);

    }
}
