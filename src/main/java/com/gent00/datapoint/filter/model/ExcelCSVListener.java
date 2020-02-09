/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ExcelCSVListener implements DataTableListener {
    private boolean printHeader = true;


    @Override
    public void receiveDataTable(DataTable df) {
        if (printHeader) {
            System.out.println(
                    df.getHeader().
                    stream().
                    sequential().
                    skip(
                            Math.max(df.getHeader().size(), df.getValues().get(0).length)
                            -
                            Math.min(df.getHeader().size(), df.getValues().get(0).length)
                    ).
                    collect(
                            Collectors.joining(","))
            );
        printHeader = false;
        }
        for (String[] values : df.getValues()) {
            System.out.println(Arrays.stream(values).sequential().collect(Collectors.joining(",")));
        }
    }
}
