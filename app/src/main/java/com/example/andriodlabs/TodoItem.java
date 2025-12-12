package com.example.andriodlabs;

public class TodoItem {

    private long id;
    private final String text;
    private final boolean urgent;

    public TodoItem(long id, String text, boolean urgent) {
        this.id = id;
        this.text = text;
        this.urgent = urgent;
    }

    public TodoItem(String text, boolean urgent) {
        this(-1, text, urgent);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return urgent;
    }
}