package com.rahimovjavlon1212.yourfeed.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rahimovjavlon1212.yourfeed.models.TopicModel;

@Database(entities = {TopicModel.class}, version = 1,exportSchema = false)
public abstract class TopicsDatabase extends RoomDatabase {
    public abstract TopicDAO getTopicDao();
}
