package pl.allblue.abdata;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.allblue.abnative.ActionsSet;
import pl.allblue.abnative.NativeAction;
import pl.allblue.abnative.NativeApp;

public class ABDatabase
{

    static public final String FilePaths_DBRequests = "db-requests.json";
    static public final String FilePaths_DeviceInfo = "device-info.json";
    static public final String FilePaths_TableIds = "table-ids.json";

    static private DatabaseHelper DatabaseHelperInstance = null;


    public SQLiteDatabase db = null;
    public ActionsSet nativeActions = null;

    public Long deviceInfo_DeviceId = null;
    public String deviceInfo_DeviceHash = null;
    public Long deviceInfo_LastUpdate = null;

    public ABDatabase(final Context context, NativeApp nativeApp)
    {
        if (ABDatabase.DatabaseHelperInstance == null)
            ABDatabase.DatabaseHelperInstance = new DatabaseHelper(context);
        this.db = ABDatabase.DatabaseHelperInstance.getWritableDatabase();
        final SQLiteDatabase db = this.db;
        final ABDatabase self = this;

        this.deviceInfo_Load(context);

        this.nativeActions = new ActionsSet()
            .addNative("GetTableColumns", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    JSONArray columns = new JSONArray();
                    result.put("columns", columns);

                    try {
                        Cursor c = db.rawQuery("PRAGMA TABLE_INFO('" +
                                        args.getString("tableName") + "')",
                                null);
                        while (c.moveToNext()) {
                            JSONArray column = new JSONArray();
                            column.put(c.getString(1));
                            column.put(c.getString(2));
                            column.put(c.getInt(3));
                            columns.put(column);
                        }

                        result.put("columns", columns);
                        result.put("error", JSONObject.NULL);
                    } catch (SQLiteException e) {
                        result.put("columns", JSONObject.NULL);
                        result.put("error", e.getMessage());
                    }

                    return result;
                }

            })
            .addNative("GetTableNames", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    JSONArray tableNames = new JSONArray();

                    Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE" +
                            " type ='table'" +
                            " AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%'", null);
                    while (c.moveToNext())
                        tableNames.put(c.getString(0));

                    result.put("tableNames", tableNames);
                    result.put("error", JSONObject.NULL);

                    return result;
                }

            })
            .addNative("Transaction_Finish", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    try {
                        if (args.getBoolean("commit")) {
                            Log.d("ABDatabase", "Successful Transaction");
                            db.setTransactionSuccessful();
                        } else {
                            Log.d("ABDatabase", "Failed Transaction");
                        }

                        db.endTransaction();

                        result.put("success", true);
                        result.put("error", JSONObject.NULL);
                    } catch (SQLException e) {
                        result.put("success", false);
                        result.put("error", e.toString());
                    }

                    return result;
                }

            })
            .addNative("Transaction_IsAutocommit", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    try {
                        result.put("result", !db.inTransaction());
                        result.put("error", JSONObject.NULL);
                    } catch (SQLException e) {
                        result.put("result", true);
                        result.put("error", e.toString());
                    }

                    return result;
                }

            })
            .addNative("Transaction_Start", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    Log.d("ABDatabase", "Starting transaction");

                    try {
                        db.beginTransaction();

                        result.put("success", true);
                        result.put("error", JSONObject.NULL);
                    } catch (SQLException e) {
                        result.put("success", false);
                        result.put("error", e.toString());
                    }

                    return result;
                }

            })
            .addNative("Query_Execute", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    try {
                        db.execSQL(args.getString("query"));

                        Log.d("ABDatabase", args.getString("query"));

                        result.put("success", true);
                        result.put("error", JSONObject.NULL);
                    } catch (SQLException e) {
                        result.put("success", false);
                        result.put("error", e.toString());
                    }

                    return result;
                }

            })
            .addNative("Query_Select", new NativeAction() {

                @Override
                public JSONObject call(JSONObject args) throws JSONException {
                    JSONObject result = new JSONObject();

                    JSONArray rows = new JSONArray();
                    JSONArray columnTypes = args.getJSONArray("columnTypes");

                    try {
                        Cursor c = db.rawQuery(args.getString("query"),
                                null);
                        while (c.moveToNext()) {
                            JSONArray row = new JSONArray();
                            for (int i = 0; i < columnTypes.length(); i++) {
                                if (c.isNull(i))
                                    row.put(JSONObject.NULL);
                                else if (columnTypes.getString(i).equals("Bool")) {
//                                    Log.d("Test", Integer.toString(c.getInt(i)));
                                    row.put(c.getInt(i) == 1);
                                } else if (columnTypes.getString(i).equals("Float"))
                                    row.put(c.getFloat(i));
                                else if (columnTypes.getString(i).equals("Id"))
                                    row.put(c.getLong(i));
                                else if (columnTypes.getString(i).equals("Int"))
                                    row.put(c.getInt(i));
                                else if (columnTypes.getString(i).equals("Long"))
                                    row.put(c.getLong(i));
                                else if (columnTypes.getString(i).equals("String"))
                                    row.put(c.getString(i));
                                else if (columnTypes.getString(i).equals("Time"))
                                    row.put(c.getLong(i));
                                else {
                                    throw new SQLiteException("Unknown column type '" +
                                            columnTypes.getString(i) + "'.");
                                }
                            }

                            rows.put(row);
                        }

//                        result.put("success", true);
                        result.put("rows", rows);
                        result.put("error", JSONObject.NULL);
                    } catch (SQLiteException e) {
                        Log.e("ABDatabase", "Query_Select Error", e);
//                        result.put("success", false);
                        result.put("rows", JSONObject.NULL);
                        result.put("error", e.getMessage());
                    }

                    return result;
                }

            });

        nativeApp.addActionsSet("ABDatabase", this.nativeActions);
    }

    public void close()
    {
        this.db.close();
    }

    public void query_Select(String query)
    {

    }

    public void query_Execute(String query)
    {

    }

    public void showTables()
    {
        Cursor c = ABDatabase.DatabaseHelperInstance.getReadableDatabase()
                .rawQuery("SHOW TABLES;", null);
        while (c.moveToNext()) {
            List<String> cols = new ArrayList<>();
            String colsStr = "";
            for (int i = 0; i < c.getColumnCount(); i++) {
                cols.add(c.getString(i));
                colsStr += (colsStr.equals("") ? "" : ", ") + c.getString(i);
            }
        }
    }


    private void addDBRequests(Context context, JSONArray requests)
            throws JSONException, SQLiteException
    {
        JSONObject result = new JSONObject();

        JSONObject json = JSONHelper.LoadFile(context,
                ABDatabase.FilePaths_DBRequests);

        JSONArray dbRequests = null;
        Long nextId = null;

        if (!json.has("nextId"))
            nextId = 1l;
        else
            nextId = json.getLong("nextId");

        String query = "INSERT INTO _ABData_DBRequests " +
                "(Id, RequestName, ActionName, ActionArgs)" +
                " VALUES ";

        boolean firstValues = true;
        for (int i = 0; i < requests.length(); i++) {
            if (!firstValues)
                query += ",";
            else
                firstValues = false;

            JSONArray request = requests.getJSONArray(i);
            query += "(";
            query += ++nextId + ",";
            query += "'" + request.getString(0) + "',";
            query += "'" + request.getString(1) + "',";
            query += "'" + DatabaseHelper.EscapeString(
                    request.getJSONObject(2).toString()) + "'";
            query +=")";
        }

        json.put("nextId", nextId);

        if (!JSONHelper.SaveFile(context, ABDatabase.FilePaths_DBRequests,
                json))
            throw new JSONException("Cannot save 'db-requests.json'.");

        this.db.execSQL(query);
    }

    private boolean clearRequests(Context context, JSONArray requestInfos)
    {
//        try {
//            JSONObject json = JSONHelper.LoadFile(context,
//                    ABDatabase.FilePaths_Requests);
//            Long[] requestIds_Arr = new Long[requestInfos.length()];
//            for (int i = 0; i < requestInfos.length(); i++)
//                requestIds_Arr[i] = requestInfos.getJSONObject(i).getLong("id");
//
//            if (json.has("requestInfos")) {
//                JSONArray requestInfos_Added = null;
//                requestInfos_Added = json.getJSONArray("requestInfos");
//                for (int i = requestInfos_Added.length() - 1; i >= 0; i--) {
//                    Long id_Added = requestInfos_Added.getJSONObject(i)
//                            .getLong("id");
//                    if (this.inArray(id_Added, requestIds_Arr))
//                        requestInfos_Added.remove(i);
//                }
//            } else {
//                Log.e("ABDatabase", "No 'requests.json'. Ignoring clear request: " +
//                        requestInfos.toString());
//            }
//
//            return JSONHelper.SaveFile(context, ABDatabase.FilePaths_Requests,
//                    json);
//        } catch (JSONException e) {
//            Log.e("ABDatabase", "Cannot 'addRequest'.", e);
//            return false;
//        }

        return true;
    }

    private JSONArray getDBRequests(Context context)
    {
        String query = "SELECT * FROM _ABData_DBRequests";

        JSONArray rows = new JSONArray();

        Cursor c = this.db.rawQuery(query, null);
        while (c.moveToNext()) {
            JSONArray row = new JSONArray();

            row.put(c.getInt(0));
            row.put(c.getString(1));
            row.put(c.getString(2));

            try {
                row.put(new JSONObject(c.getString(3)));
            } catch (JSONException e) {
                Log.e("ABDatabase", "JSON Error", e);
                row.put(JSONObject.NULL);
            }

            rows.put(row);
        }

        return rows;
    }

    private Long getNextTableId(Context context, String tableName)
    {
        try {
            JSONObject json = JSONHelper.LoadFile(context, ABDatabase.FilePaths_TableIds);
            if (json == null)
                return null;

            JSONObject tableIds = null;
            if (!json.has("tableIds")) {
                tableIds = new JSONObject();
                json.put("tableIds", tableIds);
            } else
                tableIds = json.getJSONObject("tableIds");

            Long nextId = null;
            if (!tableIds.has(tableName))
                nextId = 1l;
            else
                nextId = tableIds.getLong(tableName);

            tableIds.put(tableName, nextId + 1);

            JSONHelper.SaveFile(context, ABDatabase.FilePaths_TableIds, json);

            if (nextId == null)
                return null;

            return nextId + (this.deviceInfo_DeviceId * 100000000);
        } catch (JSONException e) {
            Log.e("Logs", "JSONException", e);
            return null;
        }
    }

    private void deviceInfo_Load(Context context)
    {
        JSONObject json = JSONHelper.LoadFile(context,
                ABDatabase.FilePaths_DeviceInfo);
        if (json == null) {
            this.deviceInfo_DeviceId = null;
            this.deviceInfo_DeviceHash = null;
        }

        try {
            this.deviceInfo_DeviceId = json.getLong("deviceId");
            this.deviceInfo_DeviceHash = json.getString("deviceHash");
        } catch (JSONException e) {
            this.deviceInfo_DeviceId = null;
            this.deviceInfo_DeviceHash = null;
        }
    }

    private boolean deviceInfo_Save(Context context, JSONObject deviceInfo)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("deviceId", !deviceInfo.isNull("deviceId") ?
                    deviceInfo.getLong("deviceId") :
                    (this.deviceInfo_DeviceId == null ?
                    JSONObject.NULL : this.deviceInfo_DeviceId));
            json.put("deviceHash", !deviceInfo.isNull("deviceHash") ?
                    deviceInfo.getString("deviceHash") :
                    (this.deviceInfo_DeviceHash == null ?
                    JSONObject.NULL : this.deviceInfo_DeviceHash));
            json.put("lastUpdate", !deviceInfo.isNull("lastUpdate") ?
                    deviceInfo.getLong("lastUpdate") :
                    (this.deviceInfo_LastUpdate == null ?
                    JSONObject.NULL : this.deviceInfo_LastUpdate));
        } catch (JSONException e) {
            return false;
        }

        Log.d("ABDatabase", "Test: " + json.toString());

        return JSONHelper.SaveFile(context, ABDatabase.FilePaths_DeviceInfo,
                json);
    }

    private boolean inArray(Long value, Long[] arr)
    {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value))
                return true;
        }

        return false;
    }

}
