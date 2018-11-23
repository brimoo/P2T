package com.p2t.p2t;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class FileBrowserActivity extends AppCompatActivity
        implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        ListView fileView = findViewById(R.id.fileView);
        Button newFileButton = findViewById(R.id.newFileButton);

        fileView.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        Arrays.asList(getFilesDir().listFiles())
                )
        );

        fileView.setOnItemClickListener(this);
        fileView.setOnItemLongClickListener(this);
        newFileButton.setOnClickListener(this);
    }

    /**
     * Called when a file is short pressed.
     * Either expands the directory, or opens the file in the editor.
     *
     * @param adapterView the adapter for the file view.
     * @param view the file view itself.
     * @param position the index of the clicked file.
     * @param id the row id of the clicked file.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        File clickedFile = (File)adapterView.getItemAtPosition(position);

        // Don't know if this can happen
        if (clickedFile == null) {
            return;
        }

        // It's a file, open it in the editor
        if (clickedFile.isFile()) {
            // It's a file, open it in the editor
            Intent i = new Intent(getApplicationContext(), TextEditorActivity.class);
            i.putExtra("path", clickedFile.getPath());
            i.putExtra("text", getTextFromFile(clickedFile));
            startActivity(i);
            return;
        }

        // It's a folder, update the view
        File folderContents[] = clickedFile.listFiles();

        // Check if the folder is empty
        if (folderContents == null || folderContents.length == 0) {
            Toast.makeText(
                    this,
                    "The folder is empty.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Change view to new folder
        ListView fileView = (ListView) view;
        fileView.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        Arrays.asList(clickedFile.listFiles())
                )
        );
    }

    /**
     * Called when a file is long pressed.
     * Shows additional options for the pressed file.
     *
     * @param adapterView the adapter for the file view.
     * @param view the file view itself.
     * @param position the index of the clicked file.
     * @param id the row id of the clicked file.
     * @return true if this consumed the long click, false if it did not.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        // TODO: Create options menu for rename/deletion/folder move
        return false;
    }

    /**
     * Called when the new file button is pressed.
     * Opens the main activity.
     * TODO: Add option for new folder
     *
     * @param v the button view.
     */
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    /**
     * Reads the text from a file
     * @param input the file to read from.
     * @return the text contained in the file.
     */
    @NonNull
    private String getTextFromFile(File input) {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;

        // Modified https://stackoverflow.com/a/12421888
        try {
            br = new BufferedReader(new FileReader(input));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            Toast.makeText(
                    this,
                    "An error occurred when reading the file.",
                    Toast.LENGTH_SHORT
            ).show();
            Log.println(Log.ERROR, "FileBrowser", e.toString());
        }
        finally {
            // Yes this mess is actually how you do this
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.println(Log.ERROR, "FileBrowser", e.toString());
                }
            }
        }

        return text.toString();
    }
}
