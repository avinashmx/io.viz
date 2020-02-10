/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class DataFrameStack extends Stack<DataFrame> {

    List<DataFrameStackListener> listeners = new Vector<>();


    @Override
    public DataFrame push(DataFrame item) {
        DataFrame df = super.push(item);
        listeners.stream().forEachOrdered(s -> s.newDataFrameAdded(item));
        return df;
    }


    public void addListener(DataFrameStackListener frameStackListener) {
        listeners.add(frameStackListener);
    }


}
