package com.example.stickynotes.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.stickynotes.activities.AddNewShoppingListActivity;
import com.example.stickynotes.adapters.MyNoteAdapter;
import com.example.stickynotes.adapters.ShoppingListAdapter;
import com.example.stickynotes.database.MyNoteDatabase;
import com.example.stickynotes.entities.MyNoteEntities;
import com.example.stickynotes.entities.ShoppingList;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ShoppingListFragment extends Fragment {

    ImageView addShoppingList;
    public static final int REQUEST_CODE_ADD_NOTE = 1;

    RecyclerView recyclerView;
    List<ShoppingList> list;
    ShoppingListAdapter adapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        addShoppingList = view.findViewById(R.id.add_shopping_list);
        addShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), AddNewShoppingListActivity.class),REQUEST_CODE_ADD_NOTE);
            }
        });

        recyclerView = view.findViewById(R.id.shopping_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        list = new ArrayList<>();
        adapter = new ShoppingListAdapter(list);
        recyclerView.setAdapter(adapter);

        EditText inputSearch = view.findViewById(R.id.editText3);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (list.size()!=0){
                    adapter.searchShopping(editable.toString());
                }
            }
        });

        getAllShoppingList();

        return view;
    }

    private void getAllShoppingList() {
        @SuppressLint("StaticFieldLeak")
        class GetShoppingList extends AsyncTask<Void, Void, List<ShoppingList>> {

            @Override
            protected List<ShoppingList> doInBackground(Void... voids) {
                return MyNoteDatabase
                        .getMyNoteDatabase(getActivity().getApplicationContext())
                        .notesDao()
                        .getAllShoppingList();
            }

            @Override
            protected void onPostExecute(List<ShoppingList> myNoteEntities) {
                super.onPostExecute(myNoteEntities);

                if (list.size()==0){
                    list.addAll(myNoteEntities);
                    adapter.notifyDataSetChanged();
                }else {
                    list.add(0,myNoteEntities.get(0));
                    adapter.notifyItemInserted(0);
                }

                recyclerView.smoothScrollToPosition(0);
            }
        }
        new GetShoppingList().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE_ADD_NOTE && resultCode==RESULT_OK){
            getAllShoppingList();
        }
    }

}