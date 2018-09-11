package com.example.waleed.telegramapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OpenUrlsActivity extends AppCompatActivity {
        public static final String LINK1_URL="http://reviewico.io/";
        public static final String LINK2_URL="http://test002.sangsangbiz.com/";
        public static final String LINK3_URL="https://open.kakao.com/o/gqnC0JM";
        public static final String LINK4_URL="https://cafe.naver.com/2018startup";
        public static final String LINK5_URL="https://open.kakao.com/o/gUhX1eR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_urls);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.urls_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.item_link1:
                 searchWeb(LINK1_URL);
                 break;
            case R.id.item_link2:
                searchWeb(LINK2_URL);
                break;
            case R.id.item_link3:
                searchWeb(LINK3_URL);
                break;
            case R.id.item_link4:
                searchWeb(LINK4_URL);
                break;
            case R.id.item_link5:
                searchWeb(LINK5_URL);
                break;
             default:
                 Toast.makeText(this, "The Url is not exist in Menu", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
     public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
         intent.putExtra(SearchManager.QUERY, query);
         if (intent.resolveActivity(getPackageManager()) != null) {
             startActivity(intent);
       }
    }
     }





