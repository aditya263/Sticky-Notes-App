package com.example.stickynotes.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.stickynotes.R;
import com.example.stickynotes.activities.AddNewNotes;
import com.example.stickynotes.adapters.MyNoteAdapter;
import com.example.stickynotes.database.MyNoteDatabase;
import com.example.stickynotes.entities.MyNoteEntities;
import com.example.stickynotes.listeners.MyNoteListeners;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyNotesFragment extends Fragment implements MyNoteListeners {

    ImageView addNotes;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int UPDATE_NOTE = 2;
    public static final int SHOW_NOTE = 3;

    private int clickedPosition = -1;

    private RecyclerView noteRec;
    private List<MyNoteEntities> noteEntitiesList;
    private MyNoteAdapter myNoteAdapter;

    public MyNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        addNotes = view.findViewById(R.id.add_notes);
        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), AddNewNotes.class), REQUEST_CODE_ADD_NOTE);
            }
        });

        noteRec = view.findViewById(R.id.note_rec);
        noteRec.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteEntitiesList = new ArrayList<>();
        myNoteAdapter = new MyNoteAdapter(noteEntitiesList, this);
        noteRec.setAdapter(myNoteAdapter);

        EditText inputSearch = view.findViewById(R.id.editText3);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myNoteAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (noteEntitiesList.size()!=0){
                    myNoteAdapter.searchNote(editable.toString());
                }
            }
        });

        getAllNotes(SHOW_NOTE,false);

        return view;
    }

    private void getAllNotes(final int requestCode,final boolean isNoteDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<MyNoteEntities>> {

            @Override
            protected List<MyNoteEntities> doInBackground(Void... voids) {
                return MyNoteDatabase
                        .getMyNoteDatabase(getActivity().getApplicationContext())
                        .notesDao()
                        .getAllNotes();
            }

            @Override
            protected void onPostExecute(List<MyNoteEntities> myNoteEntities) {
                super.onPostExecute(myNoteEntities);

                if (requestCode == SHOW_NOTE) {
                    noteEntitiesList.addAll(myNoteEntities);
                    myNoteAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteEntitiesList.add(0, myNoteEntities.get(0));
                    myNoteAdapter.notifyItemInserted(0);
                    noteRec.smoothScrollToPosition(0);
                }else if (requestCode==UPDATE_NOTE){
                    noteEntitiesList.remove(clickedPosition);

                    if (isNoteDeleted){
                        myNoteAdapter.notifyItemRemoved(clickedPosition);
                    }else {
                        noteEntitiesList.add(clickedPosition,myNoteEntities.get(clickedPosition));
                        myNoteAdapter.notifyItemChanged(clickedPosition);
                    }
                }
            }
        }
        new GetNoteTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getAllNotes(REQUEST_CODE_ADD_NOTE,false);
        } else if (requestCode == UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getAllNotes(UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }

    @Override
    public void myNoteClick(MyNoteEntities myNoteEntities, int position) {
        clickedPosition = position;
        Intent intent = new Intent(getContext().getApplicationContext(), AddNewNotes.class);
        intent.putExtra("updateOrView", true);
        intent.putExtra("myNotes", myNoteEntities);
        startActivityForResult(intent, UPDATE_NOTE);
    }
}