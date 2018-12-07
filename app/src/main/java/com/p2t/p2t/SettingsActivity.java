package com.p2t.p2t;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity{
    private int theme;
    @Override
    protected void onResume()
    {
        super.onResume();
        if(theme!=CurrentSettings.getMode()) {
            theme = CurrentSettings.getMode();
            setTheme(theme);

            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = CurrentSettings.getMode();
        setTheme(theme);
        setContentView(R.layout.activity_settings);
        TextView text =  (TextView)findViewById(R.id.textView);
        text.setText("Dark Mode");
        final Switch modeSwitch = findViewById(R.id.modeSwitch);
        modeSwitch.setChecked(CurrentSettings.getModeBool());
        modeSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                CurrentSettings.setMode(isChecked);
                setTheme(CurrentSettings.getMode());
                recreate();
            }
        });
        final FloatingActionButton loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
                return true;
            case R.id.files:
                Intent files = new Intent(getApplicationContext(), FileBrowserActivity.class);
                startActivity(files);
                return true;
            case R.id.settings:
                return true; //we are already here
        }
        return(super.onOptionsItemSelected(item));
    }

}
