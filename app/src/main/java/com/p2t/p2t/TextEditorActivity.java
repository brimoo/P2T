package com.p2t.p2t;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TextEditorActivity extends AppCompatActivity implements View.OnClickListener{
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
        setContentView(R.layout.activity_text_editor);

        FloatingActionButton saveTextButton = findViewById(R.id.saveTextButton);
        saveTextButton.setOnClickListener(this);

        String text = getIntent().getStringExtra("text");

        if (text.isEmpty()) {
            text = "No text found";
        }

        setText(text);

        saveTextButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    private void setText(String msg) {
        EditText editText = findViewById(R.id.textPreview);
        editText.setText(msg);
    }

    /**
     * Called when the save button is pressed.
     * Saves the text to a file and moves to the FileBrowserActivity
     * @param v the button's view.
     */
    @Override
    public void onClick(View v) {
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        EditText editText = findViewById(R.id.textPreview);
        String path;

        if (getIntent().getStringExtra("path") != null) {
            path = getIntent().getStringExtra("path");
        } else {
            path = "new_" + timestamp + ".txt";
        }

        saveTextToFile(editText.getText().toString(), new File(path));

        Intent i = new Intent(getApplicationContext(), FileBrowserActivity.class);
        startActivity(i);
    }

    /**
     * Saves the text to the provided file
     *
     * @param text the text to save.
     * @param saveFile the file to save to.
     */
    private void saveTextToFile(String text, File saveFile) {
        FileOutputStream stream = null;
        // Open file for writing
        try {
            stream = new FileOutputStream(saveFile);
        }
        catch (FileNotFoundException e) {
            try {
                stream = openFileOutput(saveFile.getPath(), Context.MODE_PRIVATE);
            }
            catch (FileNotFoundException f) {
                Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
                Log.println(Log.ERROR, "TextEditor", f.toString());
                return;
            }
        }

        // Write text to file
        try {
            stream.write(text.getBytes());
        }
        catch (IOException e) {
            Toast.makeText(this, "Unable to write to file.", Toast.LENGTH_SHORT).show();
            Log.println(Log.ERROR, "TextEditor", e.toString());
            return;
        }
        finally {
            try {
                stream.close();
            }
            catch (IOException e) {
                Toast.makeText(this, "Unable to close file.", Toast.LENGTH_SHORT).show();
                Log.println(Log.ERROR, "TextEditor", e.toString());
            }
        }

        // Save the file
        try {
            saveFile.createNewFile();
        }
        catch (IOException e) {
            // Toast.makeText(this, "Unable to save file.", Toast.LENGTH_SHORT).show();
            Log.println(Log.ERROR, "TextEditor", e.toString());
        }
    }
}
