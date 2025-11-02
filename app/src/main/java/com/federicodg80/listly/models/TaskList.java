package com.federicodg80.listly.models;

public class TaskList {
    private int ListId;
    private String Title;
    private String Description;
    private String Icon;
    private String OwnerUid;

    public TaskList(int listId, String title, String description, String icon, String ownerUid) {
        ListId = listId;
        Title = title;
        Description = description;
        Icon = icon;
        OwnerUid = ownerUid;
    }

    public int getListId() {
        return ListId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getIcon() {
        return Icon;
    }

    public String getOwnerUid() {
        return OwnerUid;
    }
}
