/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRow;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

class IOStatFilterStreamerTest {

    @Test
    void parseHeaderToColumns() {
        String header1 = "avg-cpu:  %user   %nice %system %iowait  %steal   %idle";
        String header2 = "Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util";
        Assertions.assertArrayEquals(new String[]{"avg-cpu", "%user", "%nice", "%system", "%iowait", "%steal", "%idle"}, IOStatFilterStreamer.parseStringToColumns(header1));
        Assertions.assertArrayEquals(new String[]{"Device", "rrqm/s", "wrqm/s", "r/s", "w/s", "rkB/s", "wkB/s", "avgrq-sz", "avgqu-sz", "await", "r_await", "w_await", "svctm", "%util"},
                IOStatFilterStreamer.parseStringToColumns(header2));

    }
}