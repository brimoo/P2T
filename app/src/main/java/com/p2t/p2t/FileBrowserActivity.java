package com.p2t.p2t;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;


public class FileBrowserActivity extends AppCompatActivity

        implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, View.OnClickListener {
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
        setContentView(R.layout.activity_file_browser);

        ListView fileView = findViewById(R.id.fileView);
        FloatingActionButton newFileButton = findViewById(R.id.newFileButton);

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
                // We're already on the file viewer
                return true;
            case R.id.settings:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
        }
        return(super.onOptionsItemSelected(item));
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
        updateList(clickedFile);
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
        final CharSequence[] options = { "Rename", "Delete", "Move", "Cancel" };

        final File clickedFile = (File)adapterView.getItemAtPosition(position);

        // Don't know if this can happen
        if (clickedFile == null) {
            return true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                switch (which) {
                    case 0: // Rename
//                        renameFile(clickedFile, "Test");
                        renameAlert(clickedFile);
                        break;
                    case 1: // Delete
                        deleteFile(clickedFile);
                        break;
                    case 2: // Move
                        renameFile(clickedFile, "Test");
                        break;
                    default:
                        dialog.cancel();
                }
                updateList(clickedFile);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        return true;
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

    private void renameFile(File file, String newName) {
        String newPath = file.getParent() + "/" + newName;
        boolean success = file.renameTo(new File(newPath));
        if (!success) {
            Toast.makeText(this, "Rename failed", Toast.LENGTH_SHORT).show();
            Log.println(Log.ERROR, "FileBrowser", "Rename failed for " + newPath.toString());
        }

        updateList(file);
    }

    private void deleteFile(File file) {
        boolean success;

        if (file.isDirectory()) {
            for(File subFile : file.listFiles()) {
                subFile.delete();
            }
        }

        success = file.delete();
        if (!success) {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            Log.println(Log.ERROR, "FileBrowser", "Delete failed for " + file.getPath().toString());
        }

    }

    /**
     * Updates the list of files to the passed directory.
     * @param currentFile the directory to change the view to.
     */
    private void updateList(File currentFile) {
        File[] folderContents = currentFile.listFiles();

        // Check if the folder is empty
        while (folderContents == null || folderContents.length == 0) {
            folderContents = currentFile.getParentFile().listFiles();
        }

        // Change view to new folder
        ListView fileView = findViewById(R.id.fileView);;
        fileView.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        Arrays.asList(folderContents)
                )
        );
    }

    /**
     * Creates a dialog to rename a file.
     * @param file the file to rename.
     */
    private void renameAlert(final File file) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                renameFile(file, value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    /**
     * Creates a dialog to move a file to a new folder.
     * @param file the file to move.
     */
    private void moveAlert(final File file) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Move to");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final ListView folderList = new ListView(this);
        alert.setView(folderList);
        folderList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                getFolderList(file.getParentFile())
        ));

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = "--Test--";
                renameFile(file, value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    /**
     * Prompts the user to enter the name for a new folder, then creates it.
     * @param currentDir the directory to create the folder under.
     */
    private void newFolderAlert(final File currentDir) {

    }

    /**
     * Retrieves a list of all folders inside the current directory
     * @param currentDir the directory to check.
     * @return a list of folders inside of the checked directory.
     */
    private ArrayList<File> getFolderList(File currentDir) {
        if (!currentDir.isDirectory()) {
            throw new InvalidParameterException();
        }

        ArrayList<File> ret = new ArrayList<>();

        for (File file : currentDir.listFiles()) {
            if (file.isDirectory()) {
                ret.add(file);
            }
        }

        return ret;
    }
}
