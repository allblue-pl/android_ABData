package pl.allblue.abdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.allblue.abdatabase.SelectColumnType;

public class ABTable {

    private ABTableColumn[] columns;

    public ABTable(ABTableColumn[] columns) {
        this.columns = columns;
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].name == columnName)
                return i;
        }

        throw new AssertionError("Column '" + columnName +
                "' does not exist.");
    }

    public int getColumnsCount() {
        return columns.length;
    }

    public String getSelect_ColumnNames(String tableAlias, String prefix) {
        String select = "";
        for (int i = 0; i < columns.length; i++) {
            if (i > 0)
                select += ",";
            if (tableAlias != null)
                select += tableAlias + ".";
            select += columns[i].name;
            if (prefix != null) {
                select += " AS " + prefix + columns[i].name;
            }
        }

        return select;
    }

    public String getSelect_ColumnNames(String tableAlias) {
        return getSelect_ColumnNames(tableAlias, null);
    }

    public String getSelect_ColumnNames() {
        return getSelect_ColumnNames(null);
    }

    public List<SelectColumnType> getSelect_ColumnTypes() {
        List<SelectColumnType> columnTypes = new ArrayList<>();
        for (int i = 0; i < columns.length; i++)
            columnTypes.add(columns[i].type);

        return columnTypes;
    }

//    public class Column {
//        private String name;
//        private String type;
//
//        public Column(String name, String type) {
//            name = name;
//            type = type;
//        }
//    }


    static abstract public class Row {
//        protected ABTable table;

        private int columnsCount;

        protected JSONArray jRow;
        protected int offset;

        public Row(JSONArray jRow, int columnsCount, int offset) {
//            table = table;
            this.jRow = jRow;
            this.columnsCount = columnsCount;
            this.offset = offset;
        }

        public Row(JSONArray json, int columnsCount) {
            this(json, columnsCount, 0);
        }

        public int getColumnsCount() {
            return columnsCount;
        }

        abstract public JSONObject getAsJSONObject();
        abstract public JSONObject getAsJSONObject(String prefix);
        abstract public void setFromJSONObject(JSONObject json,
                String prefix, int offset);
        abstract public void setFromJSONObject(JSONObject json,
                String prefix);
        abstract public void setFromJSONObject(JSONObject json,
                int offset);
        abstract public void setFromJSONObject(JSONObject json);
    }

}
