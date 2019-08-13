package com.example.chatclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.manojbhadane.QButton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.roger.catloadinglibrary.CatLoadingView;

public class signupclass extends AppCompatActivity {

    MaterialTextField layout_et3,layout_et4,layout_et5;
    EditText et3,et4,et5;
    QButton but2;
    CatLoadingView catLoadingView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        et3=findViewById(R.id.et3);
        et4=findViewById(R.id.et4);
        et5=findViewById(R.id.et5);

        catLoadingView=new CatLoadingView();

        layout_et3=findViewById(R.id.layout_et3);
        layout_et4=findViewById(R.id.layout_et4);
        layout_et5=findViewById(R.id.layout_et5);
        but2=findViewById(R.id.but2);


        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user=new ParseUser();
                user.setEmail(et3.getText().toString());
                user.setUsername(et4.getText().toString());
                user.setPassword(et5.getText().toString());
                catLoadingView.show(getSupportFragmentManager(),"");
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                            editor.putString("username",et4.getText().toString());
                            editor.putString("password",et5.getText().toString());
                            editor.apply();
                            Intent intent1=new Intent(signupclass.this,chatit.class);
                            startActivity(intent1);
                            catLoadingView.dismiss();
                        }else {e.getStackTrace();}
                    }
                });

            }


        });


    }

}
