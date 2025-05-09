package edu.sjsu.cs157a.forum.model;

import java.util.*;

public class Element {
    private String table;
    private String primaryKeyColumn;
    private Object primaryKeyValue;
    private String[] columnNames;
    private Object[] columnValues;
    public Element (String table, String primaryKeyColumn, Object primaryKeyValue, String[] columnNames, Object[] columnValues){
        this.table = table;
        this.primaryKeyColumn = primaryKeyColumn;
        this.columnNames = columnNames;
        this.columnValues = columnValues;
        this.primaryKeyValue = primaryKeyValue;
    }

    public String getPrimaryKeyValueString(){
        return String.valueOf(primaryKeyValue);
    }
    public Object getPrimaryKeyValue(){
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(Object newValue){
        primaryKeyValue = newValue;
    }

    public String getPrimaryKeyColumn(){
        return primaryKeyColumn;
    }

    public Iterator<Object> iterator (){
        return Arrays.stream(columnValues).iterator();
    }

    public String getColumnsNames() {
        return "(" + String.join(", ", columnNames) + ")";
    }

    public String[] getColumnNamesArray(){
        return columnNames;
    }

    public Object[] getColumnValues(){
        return columnValues;
    }


    public String getColumnsQuestionMark() {
        return "(" + String.join(", ", Collections.nCopies(columnNames.length, "?")) + ")";
    }


    public String getTable(){
        return table;
    }

    public String toString(){
        return getTable() +": "+ getPrimaryKeyColumn()+ ": "+ getPrimaryKeyValueString();
    }
    //Experimental Utility, Tested but not used
    public void setColumnValue(String columnName, Object value) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                columnValues[i] = value;
                return;
            }
        }
        throw new IllegalArgumentException("Column not found: " + columnName);
    }

    public Object getColumnValue(String columnName) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                return columnValues[i];
            }
        }
        throw new IllegalArgumentException("Column not found: " + columnName);
    }
    public static Element fromMap(String table, String primaryKeyColumn, Map<String, Object> row) {
        if (!row.containsKey(primaryKeyColumn)) {
            throw new IllegalArgumentException("Map does not contain primary key: " + primaryKeyColumn);
        }
        List<String> colNames = new ArrayList<>();
        List<Object> colValues = new ArrayList<>();

        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (!entry.getKey().equals(primaryKeyColumn)) {
                colNames.add(entry.getKey());
                colValues.add(entry.getValue());
            }
        }
        return new Element(table, primaryKeyColumn, row.get(primaryKeyColumn), colNames.toArray(new String[0]),
                colValues.toArray());
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(primaryKeyColumn, primaryKeyValue);

        for (int i = 0; i <  columnNames.length; i++) {
            map.put(columnNames[i], columnValues[i]);
        }
        return map;
    }
}

