package com.p2t.p2t;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TextEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);

        String message = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        setText(message);
    }

    private void setText(String msg) {
        EditText editText = findViewById(R.id.textPreview);
        editText.setText(msg);
    }
}
