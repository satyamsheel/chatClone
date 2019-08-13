package com.example.chatclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.manojbhadane.QButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.roger.catloadinglibrary.CatLoadingView;


public class MainActivity extends AppCompatActivity {
    MaterialTextField layout_et1,layout_et2;
    EditText et1,et2;
    TextView tv1;
    QButton but1;
    CatLoadingView cat;
    String name,pass;
    Button push;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        cat=new CatLoadingView();

        layout_et1=findViewById(R.id.layout_et1);
        layout_et2=findViewById(R.id.layout_et2);
        but1=findViewById(R.id.but1);


        tv1=findViewById(R.id.tv1);


        SharedPreferences pref =getSharedPreferences("user",MODE_PRIVATE);
             name=pref.getString("username","");
             pass=pref.getString("password","");
             System.out.print(" satyam "+ name);
             System.out.println(pass);
             if(!name.equals("") && !pass.equals(""))
             {
            et1.setText(name);
            et2.setText(pass);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    but1.performClick();
                }
            },0000);
        }


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,signupclass.class);
                startActivity(intent);
            }
        });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat.show(getSupportFragmentManager(),"");
                ParseUser.logInInBackground(et1.getText().toString(), et2.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null)
                        {
                            SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                            editor.putString("username",et1.getText().toString());
                            editor.putString("password",et2.getText().toString());
                            editor.apply();
                            Intent intent=new Intent(MainActivity.this,chatit.class);
                            startActivity(intent);
                            cat.dismiss();
                        }
                        else{e.getStackTrace();}
                    }
                });

            }
        });
    }
}
