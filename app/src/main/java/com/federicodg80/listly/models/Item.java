package com.federicodg80.listly.models;

import androidx.annotation.NonNull;

public class Item  {
    private int ItemId;
    private int ListId;
    private String Title;
    private boolean Completed;
    private String Description;
    private String Notes;
    private String CheckedBy;
    private Integer Quantity;
    private String Unit;

    public Item(int itemId, int listId, String title, boolean completed, String description, String notes, String checkedBy, Integer quantity, String unit) {
        ItemId = itemId;
        ListId = listId;
        Title = title;
        Completed = completed;
        Description = description;
        Notes = notes;
        CheckedBy = checkedBy;
        Quantity = quantity;
        Unit = unit;
    }

    public Item(int listId, String title, String description, String notes, Integer quantity, String unit) {
        ListId = listId;
        Title = title;
        Description = description;
        Notes = notes;
        Quantity = quantity;
        Unit = unit;
    }

    public Item(int itemId, int listId, String title, String description, String notes, Integer quantity, String unit) {
        ItemId = itemId;
        ListId = listId;
        Title = title;
        Description = description;
        Notes = notes;
        Quantity = quantity;
        Unit = unit;
    }

    public int getItemId() {
        return ItemId;
    }

    public int getListId() {
        return ListId;
    }

    public String getTitle() {
        return Title;
    }

    public boolean isCompleted() {
        return Completed;
    }

    public String getDescription() {
        return Description;
    }

    public String getNotes() {
        return Notes;
    }

    public String getCheckedBy() {
        return CheckedBy;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public String getUnit() {
        return Unit;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + ItemId +
                ", listId=" + ListId +
                ", title='" + Title + '\'' +
                ", completed=" + Completed +
                ", description='" + Description + '\'' +
                ", checkedBy='" + CheckedBy + '\'' +
                ", quantity=" + Quantity +
                ", unit='" + Unit + '\'' +
                ", notes='" + Notes + '\'' +
                '}';
    }
}
