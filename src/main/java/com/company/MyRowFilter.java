package com.company;

import javax.swing.*;

public class MyRowFilter extends RowFilter {
    private String searchText;

    MyRowFilter(String searchText)
    {
        this.searchText = searchText;
    }

    @Override
    public boolean include(Entry entry) {
        return entry.getStringValue(0).contains(searchText);
    }
}
