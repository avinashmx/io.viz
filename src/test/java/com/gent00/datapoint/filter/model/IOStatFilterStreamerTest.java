/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IOStatFilterStreamerTest {

    @Test
    void parseHeaderToColumns() {
        String header1 = "avg-cpu:  %user   %nice %system %iowait  %steal   %idle";
        String header2 = "Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util";
        Assertions.assertArrayEquals(new String[]{"avg-cpu", "%user", "%nice", "%system", "%iowait", "%steal", "%idle"}, IOStatFilterStreamer.parseStringToColumns(header1));
        Assertions.assertArrayEquals(new String[]{"Device","rrqm/s","wrqm/s","r/s","w/s","rkB/s","wkB/s","avgrq-sz","avgqu-sz","await","r_await","w_await","svctm","%util"},
                IOStatFilterStreamer.parseStringToColumns(header2));


    }
}