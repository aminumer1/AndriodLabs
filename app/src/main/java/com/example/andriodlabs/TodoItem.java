package com.example.andriodlabs;

public class TodoItem {
    private final String text;
    private final boolean urgent;

    public TodoItem(String text, boolean urgent) {
        this.text = text;
        this.urgent = urgent;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return urgent;
    }
}