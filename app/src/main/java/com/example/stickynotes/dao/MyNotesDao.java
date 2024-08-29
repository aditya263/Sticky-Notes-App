package com.example.stickynotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.stickynotes.entities.MyNoteEntities;
import com.example.stickynotes.entities.MyReminderEntities;
import com.example.stickynotes.entities.ShoppingList;

import java.util.List;

@Dao
public interface MyNotesDao {

    ////////////////////////////////////Note

    @Query("SELECT * FROM note ORDER BY id DESC")
    List<MyNoteEntities> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(MyNoteEntities noteEntities);

    @Delete
    void deleteNotes(MyNoteEntities noteEntities);

    ////////////////////////////////////Reminder

    @Query("SELECT * FROM reminder ORDER BY id DESC")
    List<MyReminderEntities> getAllReminder();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(MyReminderEntities reminderEntities);

    @Delete
    void deleteReminder(MyReminderEntities reminderEntities);


    ////////////////////////////////////Note

    @Query("SELECT * FROM shoppinglist ORDER BY id DESC")
    List<ShoppingList> getAllShoppingList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShoppingList(ShoppingList shoppingList);

    @Delete
    void deleteShoppingList(ShoppingList shoppingList);


}
