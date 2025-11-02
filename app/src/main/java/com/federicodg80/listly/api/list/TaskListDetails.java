package com.federicodg80.listly.api.list;

import com.federicodg80.listly.models.Item;
import com.federicodg80.listly.models.TaskList;
import com.federicodg80.listly.models.User;

import java.util.List;

public class TaskListDetails extends TaskList {
    private List<User> Collaborators;
    private List<Item> Items;
    private User Owner;

    public TaskListDetails(int id, String title, String description, String icon, String ownerUid, User Owner, List<User> collaborators, List<Item> items, User owner) {
        super(id, title, description, icon, ownerUid);
        this.Owner = Owner;
        this.Collaborators = collaborators;
        this.Items = items;
    }

    public List<User> getCollaborators() {
        return Collaborators;
    }

    public List<Item> getItems() {
        return Items;
    }
    public User getOwner() {
        return Owner;
    }
}
