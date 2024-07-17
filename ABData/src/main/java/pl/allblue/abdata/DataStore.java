package pl.allblue.abdata;

import pl.allblue.abnative.ActionsSet;
import pl.allblue.abnative.NativeApp;

public class DataStore {

    private NativeApp nativeApp = null;
    private ActionsSet nativeActions = null;

    public DataStore(NativeApp nativeApp)
    {
        this.nativeApp = nativeApp;
    }

    public NativeApp getNativeApp()
    {
        return this.nativeApp;
    }

    public TableNative getTable(String name)
    {
        return new TableNative(this, name);
    }


}
