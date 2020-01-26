package com.gent00.datapoint.filter.model;

import com.gent00.datapoint.filter.model.IOStatFilter;

import java.util.*;

public class StatTable {
    private List<List<String>> rowcols;
    private Set<IOStatFilter.IOStatHeader> headerModel = new LinkedHashSet<>(10);
    private int groupIndex = -1;
    public StatTable(int _groupIndex) {
        rowcols = new ArrayList<>(10);
        this.groupIndex = _groupIndex;
    }

    Map<IOStatFilter.IOStatHeader,List<String>> ROWS2FOLD = new HashMap<>();
    public void populateRow(IOStatFilter.IOStatHeader header, List<String> cols) {
        if (!header.headerType.equals("MASTER")) { //Assumption only one non-Master row //FIXME
            ROWS2FOLD.put(header, cols);
        }else{
            newRow();
            int currentIdx = rowcols.size() - 1;
            rowcols.get(currentIdx).addAll(cols);
            for (IOStatFilter.IOStatHeader ioStatHeader : ROWS2FOLD.keySet()) {
                rowcols.get(currentIdx).addAll(ROWS2FOLD.get(ioStatHeader));
            }

            //Build up header for later
            headerModel.add(header);
            for (IOStatFilter.IOStatHeader ioStatHeader : ROWS2FOLD.keySet()) {
                headerModel.add(ioStatHeader);
            }

        }
    }

    public  void newRow(){
        rowcols.add(new ArrayList<>());
    }

    public List<List<String>> getModel(){
        return rowcols;
    }

    public void print(boolean includeHeader) {

        if (includeHeader) {
            boolean isFirstColumnInMasterHeader = true;
            for (IOStatFilter.IOStatHeader header : headerModel) {
                if(isFirstColumnInMasterHeader) {
                    System.out.print(header.headerName+",");
                    isFirstColumnInMasterHeader= false;
                }
                for (String column : header.columns) {
                    System.out.print(column + ",");
                }
            }
            System.out.print("group_id");
            System.out.print("\n");
        }
        for (List<String> rowcol : rowcols) {
            //For each row
            for (String val : rowcol) {
                System.out.print(val + ',');
            }
            System.out.print(groupIndex);
            System.out.print("\n");
        }
    }
}
