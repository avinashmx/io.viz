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

    public void addRow(List<String> values) {
        if (this.values == null) {
            this.values = new ArrayList<>(5);
        }
        this.values.add(values.toArray(new String[values.size()]));
    }


}
