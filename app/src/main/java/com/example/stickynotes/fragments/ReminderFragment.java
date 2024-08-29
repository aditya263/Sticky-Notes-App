package com.example.stickynotes.fragments;

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
import com.example.stickynotes.activities.AddNewReminderActivity;
import com.example.stickynotes.adapters.MyNoteAdapter;
import com.example.stickynotes.adapters.ReminderAdapter;
import com.example.stickynotes.database.MyNoteDatabase;
import com.example.stickynotes.entities.MyNoteEntities;
import com.example.stickynotes.entities.MyReminderEntities;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ReminderFragment extends Fragment {

    ImageView addReminder;
    public static final int REQUEST_CODE_ADD_NOTE = 1;

    private RecyclerView reminderRec;
    private List<MyReminderEntities> reminderEntitiesList;
    private ReminderAdapter reminderAdapter;

    public ReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        addReminder = view.findViewById(R.id.add_reminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), AddNewReminderActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });

        reminderRec=view.findViewById(R.id.reminder_rec);
        reminderRec.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        reminderEntitiesList=new ArrayList<>();
        reminderAdapter=new ReminderAdapter(reminderEntitiesList);
        reminderRec.setAdapter(reminderAdapter);

        EditText inputSearch = view.findViewById(R.id.editText3);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                reminderAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (reminderEntitiesList.size()!=0){
                    reminderAdapter.searchReminder(editable.toString());
                }
            }
        });

        getAllReminder();

        return view;
    }

    private void getAllReminder() {
        class GetAllReminder extends AsyncTask<Void,Void,List<MyReminderEntities>> {

            @Override
            protected List<MyReminderEntities> doInBackground(Void... voids) {
                return MyNoteDatabase
                        .getMyNoteDatabase(getActivity().getApplicationContext())
                        .notesDao()
                        .getAllReminder();
            }

            @Override
            protected void onPostExecute(List<MyReminderEntities> myReminderEntities) {
                super.onPostExecute(myReminderEntities);
                if (reminderEntitiesList.size()==0){
                    reminderEntitiesList.addAll(myReminderEntities);
                    reminderAdapter.notifyDataSetChanged();
                }else {
                    reminderEntitiesList.add(0,myReminderEntities.get(0));
                    reminderAdapter.notifyItemInserted(0);
                }

                reminderRec.smoothScrollToPosition(0);
            }
        }
        new GetAllReminder().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE_ADD_NOTE && resultCode==RESULT_OK){
            getAllReminder();
        }
    }

}