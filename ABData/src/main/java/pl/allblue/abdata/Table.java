package pl.allblue.abdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.allblue.abnative.OnWebResultCallback;

public class Table {

    private DataStore dataStore = null;
    private String name = null;

    public Table(DataStore dataStore, String tableName)
    {
        this.name = tableName;
        this.dataStore = dataStore;
    }

    public void select(JSONObject args, OnSelectCallback callback) throws ABDataException
    {
        JSONObject actionArgs = new JSONObject();
        try {
            actionArgs.put("tableName", this.name);
            actionArgs.put("args", args);
        } catch (JSONException e) {
            throw new ABDataException(e.getMessage(), e);
        }

        Log.d("Table", "Calling select...");
        this.dataStore.getNativeApp().callWeb("ABData", "Table_Select",
                actionArgs, new OnWebResultCallback() {
            @Override
            public void call(JSONObject result) throws JSONException {
                if (!result.isNull("error")) {
                    Log.d("ABData Error -> Select", result.getString("error"));
                    callback.onSelect(null, result.getString("error"));
                    return;
                }

                callback.onSelect(result.getJSONArray("rows"), null);
            }
        });
    }

    public void update(JSONArray rows, OnUpdateCallback callback) throws ABDataException
    {
        JSONObject actionArgs = new JSONObject();
        try {
            actionArgs.put("tableName", this.name);
            actionArgs.put("rows", rows);
        } catch (JSONException e) {
            throw new ABDataException(e.getMessage(), e);
        }

        Log.d("Table", "Calling update...");
        this.dataStore.getNativeApp().callWeb("ABData",
                "Table_Update", actionArgs, new OnWebResultCallback() {
            @Override
            public void call(JSONObject result) throws JSONException {
                if (!result.isNull("error")) {
                    Log.d("ABData Error -> Update", result.getString("error"));
                    callback.onUpdate(false, result.getString("error"));
                    return;
                }

                callback.onUpdate(result.getBoolean("success"), null);
            }
        });
    }


    public interface OnSelectCallback
    {
        void onSelect(JSONArray rows, String error) throws JSONException;
    }

    public interface OnUpdateCallback
    {
        void onUpdate(boolean success, String error) throws JSONException;
    }

}
