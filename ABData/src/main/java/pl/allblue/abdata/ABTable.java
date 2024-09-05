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
        for (int i = 0; i < this.columns.length; i++) {
            if (this.columns[i].name == columnName)
                return i;
        }

        throw new AssertionError("Column '" + columnName +
                "' does not exist.");
    }

    public String getSelect_ColumnNames(String tableAlias, String prefix) {
        String select = "";
        for (int i = 0; i < this.columns.length; i++) {
            if (i > 0)
                select += ",";
            if (tableAlias != null)
                select += tableAlias + ".";
            select += this.columns[i].name;
            if (prefix != null) {
                select += " AS " + prefix + this.columns[i].name;
            }
        }

        return select;
    }

    public String getSelect_ColumnNames(String tableAlias) {
        return this.getSelect_ColumnNames(tableAlias, null);
    }

    public String getSelect_ColumnNames() {
        return this.getSelect_ColumnNames(null);
    }

    public List<SelectColumnType> getSelect_ColumnTypes() {
        List<SelectColumnType> columnTypes = new ArrayList<>();
        for (int i = 0; i < this.columns.length; i++)
            columnTypes.add(this.columns[i].type);

        return columnTypes;
    }

//    public class Column {
//        private String name;
//        private String type;
//
//        public Column(String name, String type) {
//            this.name = name;
//            this.type = type;
//        }
//    }


    static abstract public class Row {
//        protected ABTable table;

        private int columnsCount;

        protected JSONArray json;
        protected int offset;

        public Row(JSONArray json, int columnsCount, int offset) {
//            this.table = table;
            this.json = json;
            this.columnsCount = columnsCount;
            this.offset = offset;
        }

        public Row(JSONArray json, int columnsCount) {
            this(json, columnsCount, 0);
        }

        public int getColumnsCount() {
            return this.columnsCount;
        }

        abstract public JSONObject getRowAsJSONObject();
    }

}
