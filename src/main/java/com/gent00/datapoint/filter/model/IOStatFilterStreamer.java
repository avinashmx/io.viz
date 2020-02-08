package com.gent00.datapoint.filter.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zavtech.morpheus.frame.DataFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IOStatFilterStreamer {

    private static Pattern p = Pattern.compile("^[A-Za-z0-9-_]+:");
    private Log log = LogFactory.getLog(IOStatFilterStreamer.class);


    public static void main(String args[]) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        IOStatFilterStreamer streamer = new IOStatFilterStreamer();
        streamer.process(bufferedReader);
    }

    private void process(BufferedReader reader) throws IOException {
        String line = "";

        Set<String> headers = new HashSet<>(10);
        while ((line = reader.readLine()) != null) {
            //Discard empties
            if (line.isEmpty()) {
                continue;
            }
            log.debug(line);
            Matcher m = p.matcher(line); //If it's a "header" line
            if (m.find()) {
                headers.add(line);
            }
        }

    }

    public static String[] parseHeaderToColumns(String line) {
        ArrayList<String> headerColumns = new ArrayList<>();
        Arrays.stream(line.split(" ")).//Split on spaces
                filter(s->!s.isBlank()). //Remove Blanks
                forEach(s -> headerColumns.add(s.replace(":",""))); //Add into array, but first rip out ":" as well.
        return headerColumns.toArray(new String[headerColumns.size()]);
    }


}
