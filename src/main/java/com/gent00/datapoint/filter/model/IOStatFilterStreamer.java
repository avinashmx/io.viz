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
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IOStatFilterStreamer {

    private static Pattern p = Pattern.compile("^[A-Za-z0-9-_]+:");
    private Log log = LogFactory.getLog(IOStatFilterStreamer.class);

    private DataFrameStackListener dataFrameStackListener;

    public IOStatFilterStreamer(DataFrameStackListener dataFrameStackListener) {
        this.dataFrameStackListener = dataFrameStackListener;
    }

    public static void main(String[] args) throws IOException {
        IOStatFilterStreamer ioStatFilterStreamer = new IOStatFilterStreamer(new ExcelCSVListener());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        ioStatFilterStreamer.process(bufferedReader);
        ioStatFilterStreamer.dataFrameStackListener = new ExcelCSVListener();
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
        return headerColumns.toArray(new String[0]);
    }

    private void process(BufferedReader reader) throws IOException {
        String line = "";

        /*This is our stateful dataframe*/
        DataFrameStack dataFrameStack = new DataFrameStack();
        dataFrameStack.addListener(dataFrameStackListener);



        DataFrame df = new DataFrame();
        dataFrameStack.push(df); //Preallocated one frame
        String DATA_FRAME_ANCHOR = null; //Used to anchor data frame

        DataTable dt = null;

        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;

            //Discard empties
            if (line.isEmpty()) {
                continue;
            }

            if (log.isTraceEnabled()) {
                log.trace(line);
            }

            boolean isHeader = ((p.matcher(line)).find()) || line.startsWith("Device"); //We detect it's a header by a "semi colon" or by "Device"
            if (isHeader && log.isDebugEnabled()) {
                log.debug("Header identified -> " + line);
            }

            if (isHeader) {

                //Cleanup existing DataTable since it's a new header
                {
                    df.addDataTable(dt);
                    dt = new DataTable();

                    //Write new header
                    String[] headerColumns = parseStringToColumns(line);
                    dt.addColumns(Arrays.asList(headerColumns));
                }

                //Is it a new frame also?
                if (dataFrameStack.size() > 0 && DATA_FRAME_ANCHOR == null) { //We need to find an "anchor" to signal that we're recurring. After we set it, do not scan again!
                    DataFrame previousDF = dataFrameStack.peek();
                    Iterator<DataTable> iterator = previousDF.iterateDataTables();
                    while (iterator.hasNext()) {
                        List<String> previousHeaderColumns = iterator.next().getHeader();//Is this header recurring?
                        if (previousHeaderColumns == null) continue;
                        String previousHeaderString = previousHeaderColumns.stream().collect(Collectors.joining());
                        String currentHeaderString = dt.getHeader().stream().collect(Collectors.joining());
                        if (currentHeaderString.equals(previousHeaderString)) {
                            DATA_FRAME_ANCHOR = currentHeaderString; //Save the "anchor" so this is easier next time.
                            log.debug("Header Recurrence Detected  @ L:" + lineNumber + " \t->\t " + (previousHeaderColumns.stream().collect(Collectors.joining(","))));
                            df = new DataFrame(); //We don't push this back to the stack, because this the first frame was pre-added.
                            dataFrameStack.push(dataFrameStack.pop()); //Activate the notification again
                            break;
                        }
                    }
                } else if (DATA_FRAME_ANCHOR != null) {
                    String currentHeaderString = dt.getHeader().stream().collect(Collectors.joining());
                    if (currentHeaderString.equals(DATA_FRAME_ANCHOR)) {
                        log.debug("Header Anchored at " + lineNumber);
                        dataFrameStack.push(df); //Push finished frame and create a new one.
                        df = new DataFrame();
                    }
                }
            } else { //Not a header, converting
                String[] rowValues = parseStringToColumns(line);
                if (dt == null) {
                    dt = new DataTable();
                }
                dt.addRow(rowValues);
            }
        }
        //Last sample checks
        if (!df.getDataTables().contains(dt)) {
            df.addDataTable(dt);
            dt = null; //Prevent re-use
        }

        if (!dataFrameStack.contains(df)) {
            dataFrameStack.push(df);
            df = null;//Prevent re-use
        }

    }


}
