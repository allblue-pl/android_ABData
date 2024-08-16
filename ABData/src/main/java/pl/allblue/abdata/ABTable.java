package pl.allblue.abdata;

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

    public String getSelect_ColumnNames() {
        String select = "";
        for (int i = 0; i < this.columns.length; i++) {
            if (i > 0)
                select += ",";
            select += this.columns[i].name;
        }

        return select;
    }

    public SelectColumnType[] getSelect_ColumnTypes() {
        SelectColumnType[] columnTypes =
                new SelectColumnType[this.columns.length];
        for (int i = 0; i < this.columns.length; i++)
            columnTypes[i] = this.columns[i].type;

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

}
