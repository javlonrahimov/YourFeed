package com.rahimovjavlon1212.yourfeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.rahimovjavlon1212.yourfeed.R;
import com.rahimovjavlon1212.yourfeed.database.TopicsDatabase;
import com.rahimovjavlon1212.yourfeed.models.TopicModel;

import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsItemViewHolder> {

    private List<TopicModel> topicModels;
    private TopicsDatabase topicsDatabase;

    public InterestsAdapter(Context context) {
        topicsDatabase = Room.databaseBuilder(context, TopicsDatabase.class, "TopicsDB").allowMainThreadQueries().build();
        topicModels = topicsDatabase.getTopicDao().getTopics();
    }


    @NonNull
    @Override
    public InterestsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InterestsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsItemViewHolder holder, int position) {


        holder.name.setText(topicModels.get(position).getSectionName().toUpperCase());
        holder.mSwitch.setChecked(true);

        holder.mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                topicsDatabase.getTopicDao().addTopic(topicModels.get(position));
            } else {
                topicsDatabase.getTopicDao().deleteTopic(topicModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicModels.size();
    }
}


class InterestsItemViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    Switch mSwitch;

    InterestsItemViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.textItemInterest);
        mSwitch = itemView.findViewById(R.id.switchItemInterest);
    }
}