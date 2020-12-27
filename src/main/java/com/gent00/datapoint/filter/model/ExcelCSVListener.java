/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelCSVListener implements DataFrameStackListener {
    int DF_INDEX = 0;
    private boolean printHeader = true;

    @Override
    public void newDataFrameAdded(DataFrame df) {
        if (df.getDataTables() != null && df.getDataTables().size() != 0) {
            String UTC = new SimpleDateFormat("HH:mm:ss").format(new Date());
            List<String> columns = new ArrayList<>();
            List<DataTable> nonNullTables = df.getDataTables().stream().sequential().filter(dt -> dt.getHeader() != null).collect(Collectors.toList());

            nonNullTables.stream().parallel().filter(s -> s.getHeader() != null).forEachOrdered(s -> columns.addAll(s.getHeader())); //Get columns
            columns.remove(0);//Remove avg-cpu line
            columns.add(0, "DataFrame");
            columns.add(0, "UTC");

            if (printHeader) {
                System.out.println(columns.stream().sequential().collect(Collectors.joining(",")));
                printHeader = false;
            }

            //The first data table is general CPU so repeat it
            final String cpuValues = String.join(",", nonNullTables.get(0).getValues().get(0));

            //The second data table is a set of stats
            List<String[]> diskRows = nonNullTables.get(1).getValues();
            for (String[] diskRow : diskRows) {
                System.out.println(UTC +
                        "," +
                        DF_INDEX +
                        "," +
                        cpuValues +
                        "," +
                        String.join(",", diskRow));
            }
            DF_INDEX++;
        }
    }
}
