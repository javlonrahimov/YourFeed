package com.rahimovjavlon1212.yourfeed.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topics")
public class TopicModel {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "section_id")
    private String sectionId;

    @ColumnInfo(name = "section_name")
    private String sectionName;

    public TopicModel() {
    }

    public TopicModel(long id, String sectionId, String name) {
        this.id = id;
        this.sectionId = sectionId;
        this.sectionName = name;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
