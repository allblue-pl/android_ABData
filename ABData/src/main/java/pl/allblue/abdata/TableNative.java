package pl.allblue.abdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.allblue.abnative.OnWebResultCallback;

public class TableNative {

    private DataStore dataStore = null;
    private String name = null;

    public TableNative(DataStore dataStore, String tableName)
    {
        this.name = tableName;
        this.dataStore = dataStore;
    }

    public void delete(JSONArray where, Integer transactionId,
            OnExecuteCallback callback) throws ABDataException
    {
        JSONObject actionArgs = new JSONObject();
        try {
            actionArgs.put("transactionId", transactionId == null ?
                    JSONObject.NULL : transactionId);
            actionArgs.put("tableName", this.name);
            actionArgs.put("where", where);
        } catch (JSONException e) {
            throw new ABDataException(e.getMessage(), e);
        }

        Log.d("Table", "Calling delete...");
        this.dataStore.getNativeApp().callWeb("ABData",
                "Table_Delete", actionArgs, new OnWebResultCallback() {
                    @Override
                    public void call(JSONObject result) throws JSONException {
                        if (!result.isNull("error")) {
                            Log.d("ABData Error -> Delete", result.getString(
                                    "error"));
                            callback.onExecute(result.getString(
                                    "error"));
                            return;
                        }

                        callback.onExecute(null);
                    }
                });
    }

    public void select(JSONObject args, Integer transactionId,
            OnSelectCallback callback) throws ABDataException
    {
        JSONObject actionArgs = new JSONObject();
        try {
            actionArgs.put("transactionId", transactionId == null ?
                    JSONObject.NULL : transactionId);
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

    public void update(JSONArray rows, Integer transactionId,
            OnExecuteCallback callback) throws ABDataException
    {
        Log.d("Testing", this.name + ": " + transactionId);

        JSONObject actionArgs = new JSONObject();
        try {
            actionArgs.put("transactionId", transactionId == null ?
                    JSONObject.NULL : transactionId);
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
                    callback.onExecute(result.getString("error"));
                    return;
                }

                callback.onExecute(null);
            }
        });
    }


    public interface OnSelectCallback
    {
        void onSelect(JSONArray rows, String error) throws JSONException;
    }

    public interface OnExecuteCallback
    {
        void onExecute(String error) throws JSONException;
    }

}
