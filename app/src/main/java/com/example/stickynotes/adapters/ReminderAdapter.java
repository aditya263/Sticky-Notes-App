package com.example.stickynotes.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stickynotes.R;
import com.example.stickynotes.entities.MyNoteEntities;
import com.example.stickynotes.entities.MyReminderEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    List<MyReminderEntities> reminderEntities;

    private List<MyReminderEntities> reminderSearch;
    private Timer timer;

    public ReminderAdapter(List<MyReminderEntities> reminderEntities) {
        this.reminderEntities = reminderEntities;
        reminderSearch=reminderEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.remember_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setReminder(reminderEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return reminderEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title,dateTime;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.reminder_heading);
            dateTime=itemView.findViewById(R.id.date_reminder);
            view=itemView.findViewById(R.id.view_reminder);

        }

        public void setReminder(MyReminderEntities myReminderEntities) {
            title.setText(myReminderEntities.getTitle());
            dateTime.setText(myReminderEntities.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            if (myReminderEntities.getColor()!=null){
                gradientDrawable.setColor(Color.parseColor(myReminderEntities.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#FFFB7B"));
            }
        }
    }

    public void searchReminder(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    reminderEntities = reminderSearch;
                } else {
                    ArrayList<MyReminderEntities> temp = new ArrayList<>();

                    for (MyReminderEntities entities : reminderSearch) {

                        if (entities.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || entities.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {

                            temp.add(entities);

                        }
                    }

                    reminderEntities = temp;
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
