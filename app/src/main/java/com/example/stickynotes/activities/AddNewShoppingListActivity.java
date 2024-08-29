package com.example.stickynotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stickynotes.R;
import com.example.stickynotes.database.MyNoteDatabase;
import com.example.stickynotes.entities.MyNoteEntities;
import com.example.stickynotes.entities.ShoppingList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewShoppingListActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteText;
    private TextView textDateTime, saveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shopping_list);

        initViews();

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });

    }

    private void saveNotes() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Note Title Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;

        } else if (inputNoteText.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Note Text Can't Be Empty", Toast.LENGTH_SHORT).show();
            return;

        }

        final ShoppingList myNoteEntities = new ShoppingList();
        myNoteEntities.setTitle(inputNoteTitle.getText().toString());
        myNoteEntities.setNoteText(inputNoteText.getText().toString());
        myNoteEntities.setDateTime(textDateTime.getText().toString());

        class SaveNotes extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                MyNoteDatabase
                        .getMyNoteDatabase(getApplicationContext())
                        .notesDao()
                        .insertShoppingList(myNoteEntities);

                return null;
            }

            @Override
            protected void onPostExecute(Void avoid) {
                super.onPostExecute(avoid);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNotes().execute();

    }

    private void initViews() {
        inputNoteText = findViewById(R.id.text_list);
        inputNoteTitle = findViewById(R.id.input_note_title);
        textDateTime = findViewById(R.id.textDateTime);
        saveNote = findViewById(R.id.save_note);
    }
}