package com.example.mj.hwapplication;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

//tab 출력
public class MainActivity extends ActivityGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this.getLocalActivityManager());

        TabHost.TabSpec left_tab = tabHost.newTabSpec("leftTab");
        TabHost.TabSpec right_tab = tabHost.newTabSpec("rightTab");


        left_tab.setIndicator("NATASHA");
        left_tab.setContent(new Intent(getApplicationContext(), LeftTabActivity.class));
        tabHost.addTab(left_tab);

        right_tab.setIndicator("YONG");
        right_tab.setContent(new Intent(getApplicationContext(), RightTabActivity.class));
        tabHost.addTab(right_tab);
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
        return super.onOptionsItemSelected(item);
    }
}
