
package com.example.waleed.designsapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
Button btnLogin,btnRegister,btnForgotPass;
ProgressBar mLodingIndicator;

final int CONTEXT_MENU_VIEW=1;
final int CONTEXT_MENU_EDIT=2;
final int CONTEXT_MENU_DELETE=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=(Button) findViewById(R.id.btn_login);
        btnForgotPass=(Button) findViewById(R.id.btn_reset_password);
        btnRegister=(Button) findViewById(R.id.btn_signup);
        mLodingIndicator=(ProgressBar)findViewById(R.id.progressBar_login);
    final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Whoo, snackbar!",Snackbar.LENGTH_LONG).setAction("close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    View view=snackbar.getView();
    TextView tvSnack=view.findViewById(android.support.design.R.id.snackbar_text);
    tvSnack.setTextColor(Color.GREEN);
    snackbar.setDuration(10000);
        snackbar.show();

        View mview=findViewById(android.R.id.content);
        String string="Hoho, snackbar customize";
        int textColor= Color.YELLOW;
       // inform(mview,string,textColor);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLodingIndicator.setVisibility(View.VISIBLE);
                registerForContextMenu(btnLogin);
                openContextMenu(btnLogin);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this)
                        .toBundle();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class),bundle);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    public static void inform(View mView, String string, int text_color) {
        Snackbar snack = Snackbar.make(mView.findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        snack.setAction("Close",null);
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.GREEN);
        //change background color too ?
        view.setBackgroundColor(mView.getResources().getColor(R.color.your_background_color));
        snack.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
       // super.onCreateContextMenu(menu, v, menuInfo);
        //menu.setHeaderTitle("My Context Menu");

        menu.add(Menu.NONE,CONTEXT_MENU_VIEW,Menu.NONE,"View");
        menu.add(Menu.NONE,CONTEXT_MENU_EDIT,Menu.NONE,"Edit");
        menu.add(Menu.NONE,CONTEXT_MENU_DELETE,Menu.NONE,"Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case CONTEXT_MENU_VIEW:
                Toast.makeText(this, "View clicked", Toast.LENGTH_SHORT).show();
                break;
            case CONTEXT_MENU_EDIT:
                Toast.makeText(this, "Edit is clicked", Toast.LENGTH_SHORT).show();
                break;
            case CONTEXT_MENU_DELETE:
                Toast.makeText(this, "Delete is clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
