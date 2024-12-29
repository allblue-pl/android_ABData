package pl.allblue.abdata;

import pl.allblue.abdatabase.ABDatabase;
import pl.allblue.abdatabase.SelectColumnType;

public class ABTableColumn {

    public String name;
    public SelectColumnType type;

    public ABTableColumn(String name, SelectColumnType type) {
        this.name = name;
        this.type = type;
    }

}
