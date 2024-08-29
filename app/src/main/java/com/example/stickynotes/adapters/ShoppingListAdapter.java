package com.example.stickynotes.adapters;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stickynotes.R;
import com.example.stickynotes.entities.MyReminderEntities;
import com.example.stickynotes.entities.ShoppingList;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    List<ShoppingList> lists;

    private List<ShoppingList> shoppingSearch;
    private Timer timer;

    public ShoppingListAdapter(List<ShoppingList> lists) {
        this.lists = lists;
        shoppingSearch = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setNote(lists.get(position));

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDateTime, textnote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.shopping_heading);
            textnote = itemView.findViewById(R.id.shopping_list_text);
            textDateTime = itemView.findViewById(R.id.date_shop);
        }

        public void setNote(ShoppingList shoppingList) {
            textTitle.setText(shoppingList.getTitle());
            textnote.setText(shoppingList.getNoteText());
            textDateTime.setText(shoppingList.getDateTime());
        }
    }

    public void searchShopping(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    lists = shoppingSearch;
                } else {
                    ArrayList<ShoppingList> temp = new ArrayList<>();

                    for (ShoppingList entities : shoppingSearch) {

                        if (entities.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || entities.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {

                            temp.add(entities);

                        }
                    }

                    lists = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 100);

    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
