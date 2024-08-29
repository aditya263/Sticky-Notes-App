package com.example.stickynotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.stickynotes.R;
import com.example.stickynotes.fragments.MyNotesFragment;
import com.example.stickynotes.fragments.ReminderFragment;
import com.example.stickynotes.fragments.ShoppingListFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        chipNavigationBar.setItemSelected(R.id.home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyNotesFragment()).commit();

        bottomMenu();

    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                switch (i){
                    case R.id.home:
                        fragment= new MyNotesFragment();
                        break;

                    case R.id.reminder:
                        fragment= new ReminderFragment();
                        break;

                    case R.id.shopping_list:
                        fragment= new ShoppingListFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }

    private void initView() {
        chipNavigationBar=findViewById(R.id.bottom_navigation_bar);
    }
}