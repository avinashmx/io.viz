/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DataFrame {
    private List<DataTable> tables;

    public DataFrame() {
        tables = new ArrayList<>(2);
    }

    public void addDataTable(DataTable dataTable) {
        tables.add(dataTable);
    }

    public Iterator<DataTable> iterateDataTables() {
        return tables.iterator();
    }

    public List<DataTable> getDataTables() {
        return tables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFrame dataFrame = (DataFrame) o;
        return Objects.equals(tables, dataFrame.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tables);
    }
}
