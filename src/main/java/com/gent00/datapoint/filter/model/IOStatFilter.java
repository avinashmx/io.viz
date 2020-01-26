package com.gent00.datapoint.filter.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IOStatFilter {
    final Map<String, List<String>> HEADER_2_TOKENS = Collections.<String, List<String>>synchronizedMap(
            new HashMap<String, List<String>>());
    List<IOStatHeader> headers = new ArrayList<>();
    boolean isFirstTable = true;
    private RandomAccessFile raf = null;

    public IOStatFilter(File file) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "r");
    }

    public static void main(String args[]) throws IOException {
        if (args == null || args.length < 1 || args.length > 1) {
            System.err.println("Filename is the only accepted argument.");
            System.exit(-1);
        }

        IOStatFilter ioStatFilter = new IOStatFilter(new File(args[0]));
        ioStatFilter.process();
    }

    void process() throws IOException {
        try {
            readHeadersFully();
            collectDataGroup();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    private void collectDataGroup() throws IOException {
        int z = 1;
        StatTable table = new StatTable(z);
        headers.get(0).headerType = "MASTER";

        //Build table structure
        int cols = 0;
        for (IOStatHeader header : headers) {
            cols += header.columns.size();
        }

        List<List<String>> RC = new ArrayList<>(100);

        String currentLine = "";

        IOStatHeader synchronizationHeader = null, currentHeader = null;
        while ((currentLine = raf.readLine()) != null) {
            IOStatHeader header = getHeaderForLine(currentLine);
            if (synchronizationHeader == null && header != null) {
                synchronizationHeader = header; //The header that "synchronizes" the frame
                debug("Synchronized");
            } else if (synchronizationHeader != null & header != null && header.headerName.equals(synchronizationHeader.headerName)) {
                //Time to restart the frame
                debug("Frame boundary");
                z++;
                currentHeader = null;
                if (isFirstTable) {

                    table.print(true);
                    isFirstTable = false;
                }else{
                    table.print(false);
                }
                table = new StatTable(z);
            }

            if (header != null) {//We have  header, but it's just a normal header so we can recv data on next lines.
                currentHeader = header;
            } else if (currentHeader != null) {
                if (!(currentLine.length() > 0)) {
                    debug("Discarding due to zero-eth element");
                    continue;
                }
//                System.out.println("PARSING " + currentHeader.headerName + "->" + currentLine);
                List<String> fields = Arrays.stream(currentLine.split(" ")).filter(notnull -> notnull != null && notnull.trim().length() > 1).collect(Collectors.toList());
                table.populateRow(currentHeader, fields);
            }

        }
        if (table != null) { //Last iteration here, so we won't be null, but won't be expecting sync either. DUMP IT!
            if (isFirstTable) {
                table.print(true);
            } else {
                table.print(false);
            }
            table = null;
        }
    }

    private IOStatHeader getHeaderForLine(String currentLine) {
        if (currentLine == null) {
            System.err.println("Null line passed in");
            return null;
        } else {
            for (IOStatHeader header : headers) {
                if (currentLine.startsWith(header.headerName)) {
                    return header;
                }
            }
        }
        return null;
    }

    private void readHeadersFully() throws IOException {
        Set<String> headers = new HashSet<>(10);
        Pattern p = Pattern.compile("^[A-Za-z0-9-_]+:");

        long beginning = raf.getFilePointer();
        String currentLine = "";
        while ((currentLine = raf.readLine()) != null) {
            Matcher m = p.matcher(currentLine);
            if (m.find()) {
                headers.add(currentLine);
            }
        }
        raf.seek(beginning); //Reset stream

        headers.stream().forEach(s -> debug(s));

        headers.stream().forEach(h ->
                HEADER_2_TOKENS.put(h,
                        Arrays.asList(h.split(" ")).//Split on spaces
                                stream().//Serialize
                                filter(notnull -> notnull != null && notnull.trim().length() >0). //Remove nulls and would be nulls after trim
                                collect(Collectors.toList()))//Done!
        );

        /*Explode headers to arrays*/
        for (String s : HEADER_2_TOKENS.keySet()) {
            debug(MessageFormat.format("{0} has {1} columns.", HEADER_2_TOKENS.get(s).get(0), HEADER_2_TOKENS.get(s).size()));

            IOStatHeader header = new IOStatHeader();
            header.headerName = HEADER_2_TOKENS.get(s).get(0);
            header.columns = HEADER_2_TOKENS.get(s).subList(1, HEADER_2_TOKENS.get(s).size());
            this.headers.add(header);
        }

    }

    private void debug(String debugLine) {
        if(System.getProperty("DEBUGGY","anythingbuttrue").equals("true")){
            System.err.println(debugLine);
        }
    }

    public class IOStatHeader {

        public String headerName, headerType = "";
        public List<String> columns = new ArrayList<>(10);

    }


}
