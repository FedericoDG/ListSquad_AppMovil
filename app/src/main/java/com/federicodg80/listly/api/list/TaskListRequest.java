package com.federicodg80.listly.api.list;

public class TaskListRequest {
    private String Title;
    private String Description;
    private String Type;

    public TaskListRequest(String title, String description, String type) {
        Title = title;
        Description = description;
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getType() {
        return Type;
    }

}
