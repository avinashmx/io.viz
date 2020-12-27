/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataTable {

    private List<String> header;
    private List<String[]> values;

    public DataTable() {

    }

    public List<String> getHeader() {
        return header;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void addColumns(List<String> columns) {
        if (header == null) {
            header = new ArrayList<>(columns.size());
        }
        columns.stream().sequential().forEach(header::add);
    }

    public void addRow(String[] rowValues) {
        if (this.values == null) {
            this.values = new ArrayList<>(5);
        }
        this.values.add(rowValues);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataTable dataTable = (DataTable) o;
        return Objects.equals(getHeader(), dataTable.getHeader()) &&
                Objects.equals(getValues(), dataTable.getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeader(), getValues());
    }
}
