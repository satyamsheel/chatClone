package com.example.chatclone;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class finalchat extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar1;
    ListView listview_final;
    ArrayList chatList;
    ArrayAdapter adapter;
    String selectedUser;


    Handler handler = new Handler();
    Runnable refresh;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finalchat_layout);
        toolbar1=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        selectedUser =getIntent().getStringExtra("selectedUser");
        findViewById(R.id.but10).setOnClickListener(this);




        listview_final=findViewById(R.id.listview_final);
        chatList =new ArrayList();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatList);

        listview_final.setAdapter(adapter);




        try
        {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());


            final ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            final ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {

                        for (ParseObject chatObject : objects) {

                            String waMessage = chatObject.get("waMessage") + "";

                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {

                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }


                            if (chatObject.get("waSender").equals(selectedUser)) {
                                chatList.add(waMessage);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

            });

        }catch (Exception e) {

            e.printStackTrace();
        }


        refresh = new Runnable() {
            public void run() {
                try
                {

                    ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
                    ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

                    firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
                    firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

                    secondUserChatQuery.whereEqualTo("waSender", selectedUser);
                    secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());
                    firstUserChatQuery.whereNotContainedIn("waMessage",chatList);
                    secondUserChatQuery.whereNotContainedIn("waMessage",chatList);

                    final ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
                    allQueries.add(firstUserChatQuery);
                    allQueries.add(secondUserChatQuery);

                    final ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);

                    myQuery.orderByAscending("createdAt");

                    myQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {

                                for (ParseObject chatObject : objects) {

                                    String waMessage = chatObject.get("waMessage") + "";

                                    if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {

                                        waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                                    }


                                    if (chatObject.get("waSender").equals(selectedUser)) {
                                        chatList.add(waMessage);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                    });

                }catch (Exception e) {

                    e.printStackTrace();
                }

                handler.postDelayed(refresh, 1000);
            }
        };
        handler.post(refresh);


    }

    @Override
    public void onClick(View v) {
        final EditText et10 = findViewById(R.id.et10);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient", selectedUser);
        chat.put("waMessage", et10.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    chatList.add(ParseUser.getCurrentUser().getUsername() + ": " + et10.getText().toString());
                    adapter.notifyDataSetChanged();
                    et10.setText("");
                }
            }
        });

    }
}

