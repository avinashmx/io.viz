/*
 *  Copyright (C) Avinash Ramana - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *  * Written by Avinash Ramana <bitorius@gent00.com>, 2020
 *
 */

package com.gent00.datapoint.filter.model;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IOStatFilterStreamer {

    private static Pattern p = Pattern.compile("^[A-Za-z0-9-_]+:");
    private Log log = LogFactory.getLog(IOStatFilterStreamer.class);
    private DataTableListener dataTableListener = null;

    public IOStatFilterStreamer(DataTableListener dataTableListener) {
        this.dataTableListener = dataTableListener;
    }

    public static void main(String[] args) throws IOException {
        IOStatFilterStreamer streamer = new IOStatFilterStreamer(new ExcelCSVListener());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        streamer.process(bufferedReader);
    }

    /**
     * Converts a header separated by ":" and " " into a String array
     *
     * @param line
     * @return
     */
    public static String[] parseStringToColumns(String line) {
        ArrayList<String> headerColumns = new ArrayList<>();
        Arrays.stream(line.split(" ")).//Split on spaces
                filter(s -> !s.isBlank()). //Remove Blanks
                forEach(s -> headerColumns.add(s.replace(":", ""))); //Add into array, but first rip out ":" as well.
        return headerColumns.toArray(new String[headerColumns.size()]);
    }

    private void process(BufferedReader reader) throws IOException {
        String line = "";

        /*This is our stateful dataframe*/
        List<String> _currentColumns = new ArrayList<>(10);
        List<String> _currentRows = new ArrayList<>(10);

        while ((line = reader.readLine()) != null) {
            //Discard empties
            if (line.isEmpty()) {
                continue;
            }
            if (log.isTraceEnabled()) {
                log.trace(line);
            }
            boolean isHeader = (p.matcher(line)).find();
            if (isHeader) {
                //It's a header
                String[] headerColumns = parseStringToColumns(line);


                /*But is it a new dataframe? Compare encountered header with previous header. If they're the same, we're starting to repeat*/
                {
                    String incomingHeader = Arrays.stream(headerColumns).sequential().collect(Collectors.joining());
                    String previousHeaderStart = _currentColumns.stream().sequential().limit(headerColumns.length).sequential().collect(Collectors.joining()); //Only compare up to the beginning. Remember, it's a stream, so we don't know what's coming.

                    if (incomingHeader.equals(previousHeaderStart)) { //We found a new data frame!
                        DataTable dt = new DataTable();
                        dt.addColumns(_currentColumns);
                        dt.addRow(_currentRows);
                        dataTableListener.receiveDataTable(dt);

                        log.debug("A new data frame is starting");
                        _currentRows.clear();
                        _currentColumns.clear();
                    }
                }

                Arrays.stream((headerColumns)).sequential().forEach(_currentColumns::add);
            } else {
                if (_currentColumns.size() > 0) {//Do we have values?
                    Arrays.stream((parseStringToColumns(line))).sequential().forEach(_currentRows::add);
                }
            }
        }
    }


}
