package pl.allblue.abdata;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import pl.allblue.abdatabase.ABDatabase;
import pl.allblue.abdatabase.ABDatabaseException;
import pl.allblue.abdatabase.ColumnInfo;
import pl.allblue.abnative.ActionsSet;
import pl.allblue.abnative.NativeApp;

public class ABDataDatabase
{

    public ABDatabase db = null;
    public ActionsSet nativeActions = null;

    public ABDataDatabase(final Context context, ABDatabase db,
            NativeApp nativeApp)
    {
        ABDataDatabase self = this;
        this.db = db;

        this.nativeActions = new ActionsSet()
            .addNative("GetTableColumnInfos", (args) -> {
                JSONObject result = new JSONObject();

                ColumnInfo[] columnInfos = self.db.getTableColumnInfos(
                        args.getString("tableName"));

                JSONArray columnInfos_Json = new JSONArray();
                for (int i = 0; i < columnInfos.length; i++) {
                    JSONObject columnInfo = new JSONObject();
                    columnInfo.put("name", columnInfos[i].getName());
                    columnInfo.put("type", columnInfos[i].getType());
                    columnInfo.put("notNull", columnInfos[i].isNotNull());

                    columnInfos_Json.put(columnInfo);

                    result.put("columnInfos", columnInfos_Json);
                    result.put("error", JSONObject.NULL);
                }

                return result;
            })
            .addNative("GetTableNames", (args) -> {
                JSONObject result = new JSONObject();

                JSONArray tableNames = new JSONArray(self.db.getTableNames());

                result.put("tableNames", tableNames);
                result.put("error", JSONObject.NULL);

                return result;
            })
            .addNative("Transaction_Finish", (args) -> {
                JSONObject result = new JSONObject();

                try {
                    self.db.transaction_Finish(args.getInt("transactionId"),
                            args.getBoolean("commit"));

                    result.put("success", true);
                    result.put("error", JSONObject.NULL);
                } catch (ABDatabaseException e) {
                    result.put("success", false);
                    result.put("error", e.toString());
                } catch (SQLException e) {
                    result.put("success", false);
                    result.put("error", e.toString());
                }

                return result;
            })
            .addNative("Transaction_IsAutocommit", (args) -> {
                JSONObject result = new JSONObject();

                try {
                    result.put("result", self.db.transaction_IsAutocommit());
                    result.put("error", JSONObject.NULL);
                } catch (SQLException e) {
                    result.put("result", true);
                    result.put("error", e.toString());
                }

                return result;
            })
            .addNative("Transaction_Start", (args) -> {
                JSONObject result = new JSONObject();

                try {
                    int transactionId = self.db.transaction_Start();

                    result.put("success", true);
                    result.put("transactionId", transactionId);
                    result.put("error", JSONObject.NULL);
                } catch (ABDatabaseException e) {
                    result.put("success", false);
                    result.put("transactionId", null);
                    result.put("error", e.toString());
                } catch (SQLException e) {
                    result.put("success", false);
                    result.put("transactionId", null);
                    result.put("error", e.toString());
                }

                return result;
            })
            .addNative("Query_Execute", (args) -> {
                JSONObject result = new JSONObject();

                try {
                    self.db.query_Execute(args.getString("query"),
                            args.isNull("transactionId") ?
                            null : args.getInt("transactionId"));

                    result.put("success", true);
                    result.put("error", JSONObject.NULL);
                } catch (ABDatabaseException e) {
                    result.put("success", false);
                    result.put("error", e.toString());
                } catch (SQLException e) {
                    result.put("success", false);
                    result.put("error", e.toString());
                }

                return result;
            })
            .addNative("Query_Select", (args) -> {
                JSONObject result = new JSONObject();

                JSONArray columnTypes_JSON = args.getJSONArray(
                        "columnTypes");
                String[] columnTypes = new String[columnTypes_JSON.length()];
                for (int i = 0; i < columnTypes.length; i++)
                    columnTypes[i] = columnTypes_JSON.getString(i);

                try {
                    List<JSONArray> rows = self.db.query_Select(args.getString(
                            "query"), columnTypes,
                            args.isNull("transactionId") ?
                            null : args.getInt("transactionId"));
                    JSONArray rows_JSON = new JSONArray();
                    for (int i = 0; i < rows.size(); i++)
                        rows_JSON.put(rows.get(i));

//                        result.put("success", true);
                    result.put("rows", rows_JSON);
                    result.put("error", JSONObject.NULL);
                } catch (ABDatabaseException e) {
                    Log.e("ABDatabase", "Query_Select Error", e);
//                        result.put("success", false);
                    result.put("rows", JSONObject.NULL);
                    result.put("error", e.getMessage());
                }

                return result;
            });

        nativeApp.addActionsSet("ABDatabase", this.nativeActions);
    }

}
