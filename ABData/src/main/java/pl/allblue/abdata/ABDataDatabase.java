package pl.allblue.abdata;

import android.database.SQLException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pl.allblue.abdatabase.ABDatabase;
import pl.allblue.abdatabase.ABDatabaseException;
import pl.allblue.abdatabase.ColumnInfo;
import pl.allblue.abnative.ActionResultCallback;
import pl.allblue.abnative.ActionsSet;
import pl.allblue.abnative.NativeActionCallback;
import pl.allblue.abnative.NativeApp;

public class ABDataDatabase
{

    public ABDatabase db = null;
    public ActionsSet nativeActions = null;

    public ABDataDatabase(ABDatabase db,
            NativeApp nativeApp)
    {
        ABDataDatabase self = this;
        this.db = db;

        this.nativeActions = new ActionsSet()

            .addNativeCallback("GetTableColumnInfos",
                    (args, resultCallback) -> {
                self.db.getTableColumnInfos(args.getString("tableName"),
                        new ABDatabase.TableColumnInfosResultCallback() {
                    @Override
                    public void onResult(ColumnInfo[] columnInfos) {
                        try {
                            JSONObject result = new JSONObject();

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

                            resultCallback.onResult(result);
                        } catch (JSONException e) {
                            resultCallback.onError(e);
                        }
                    }
                });
            })
//            .addNative("GetTableColumnInfos", (args) -> {
//                JSONObject result = new JSONObject();
//
//                ColumnInfo[] columnInfos = self.db.getTableColumnInfos(
//                        args.getString("tableName"));
//
//                JSONArray columnInfos_Json = new JSONArray();
//                for (int i = 0; i < columnInfos.length; i++) {
//                    JSONObject columnInfo = new JSONObject();
//                    columnInfo.put("name", columnInfos[i].getName());
//                    columnInfo.put("type", columnInfos[i].getType());
//                    columnInfo.put("notNull", columnInfos[i].isNotNull());
//
//                    columnInfos_Json.put(columnInfo);
//
//                    result.put("columnInfos", columnInfos_Json);
//                    result.put("error", JSONObject.NULL);
//                }
//
//                return result;
//            })

            .addNativeCallback("GetTableNames", (args, resultCallback) -> {
                this.db.getTableNames(new ABDatabase.TableNamesResultCallback() {
                    @Override
                    public void onResult(String[] tableNames) {
                        JSONObject result = new JSONObject();

                        try {
                            JSONArray tableNames_JSON = new JSONArray(tableNames);

                            result.put("tableNames", tableNames_JSON);
                            result.put("error", JSONObject.NULL);
                        } catch (JSONException e) {
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }
                });
            })
//            .addNative("GetTableNames", (args) -> {
//                JSONObject result = new JSONObject();
//
//                JSONArray tableNames = new JSONArray(self.db.getTableNames());
//
//                result.put("tableNames", tableNames);
//                result.put("error", JSONObject.NULL);
//
//                return result;
//            })

            .addNativeCallback("Transaction_Finish",
                    (args, resultCallback) -> {
                this.db.transaction_Finish(args.getInt("transactionId"),
                        args.getBoolean("commit"),
                        new ABDatabase.VoidResultCallback_ThrowsException() {
                    @Override
                    public void onError(Exception e) {
                        Log.w("ABDataDatabase", e.getMessage(), e);
                        JSONObject result = new JSONObject();

                        try {
                            result.put("error", e.getMessage());
                        } catch (JSONException e_JSON) {
                            Log.e("ABDataDatabase", e_JSON.getMessage(),
                                    e_JSON);
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }

                    @Override
                    public void onResult() {
                        JSONObject result = new JSONObject();

                        try {
                            result.put("error", JSONObject.NULL);
                        } catch (JSONException e) {
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }
                });
            })
//            .addNative("Transaction_Finish", (args) -> {
//                JSONObject result = new JSONObject();
//
//                try {
//                    self.db.transaction_Finish(args.getInt("transactionId"),
//                            args.getBoolean("commit"));
//
//                    result.put("success", true);
//                    result.put("error", JSONObject.NULL);
//                } catch (ABDatabaseException e) {
//                    result.put("success", false);
//                    result.put("error", e.toString());
//                } catch (SQLException e) {
//                    result.put("success", false);
//                    result.put("error", e.toString());
//                }
//
//                return result;
//            })

            .addNativeCallback("Transaction_IsAutocommit",
                    (args, resultCallback) -> {
                this.db.transaction_IsAutocommit(
                        new ABDatabase.IsAutocommitResultCallback() {
                    @Override
                    public void onError(Exception e) {
                        resultCallback.onError(e);
                    }

                    @Override
                    public void onResult(boolean isAutocommit) {
                        JSONObject result = new JSONObject();

                        try {
                            result.put("result", isAutocommit);
                            result.put("error", JSONObject.NULL);
                        } catch (Exception e) {
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }
                });
            })
//            .addNative("Transaction_IsAutocommit", (args) -> {
//                JSONObject result = new JSONObject();
//
//                try {
//                    result.put("result", self.db.transaction_IsAutocommit());
//                    result.put("error", JSONObject.NULL);
//                } catch (SQLException e) {
//                    result.put("result", true);
//                    result.put("error", e.toString());
//                }
//
//                return result;
//            })

            .addNativeCallback("Transaction_Start",
                    (args, resultCallback) -> {
                this.db.transaction_Start(
                        new ABDatabase.StartTransactionResultCallback() {
                    @Override
                    public void onError(Exception e) {
                        Log.w("ABDataDatabase", e.getMessage(), e);
                        JSONObject result = new JSONObject();

                        try {
                            result.put("error", e.getMessage());
                        } catch (JSONException e_JSON) {
                            Log.e("ABDataDatabase", e_JSON.getMessage(),
                                    e_JSON);
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }

                    @Override
                    public void onResult(int transactionId) {
                        JSONObject result = new JSONObject();

                        try {
                            result.put("transactionId", transactionId);
                            result.put("error", JSONObject.NULL);
                        } catch (JSONException e) {
                            resultCallback.onError(e);
                        }

                        resultCallback.onResult(result);
                    }
                });
            })
//            .addNative("Transaction_Start", (args) -> {
//                JSONObject result = new JSONObject();
//
//                try {
//                    int transactionId = self.db.transaction_Start();
//
//                    result.put("success", true);
//                    result.put("transactionId", transactionId);
//                    result.put("error", JSONObject.NULL);
//                } catch (ABDatabaseException e) {
//                    result.put("success", false);
//                    result.put("transactionId", null);
//                    result.put("error", e.toString());
//                } catch (SQLException e) {
//                    result.put("success", false);
//                    result.put("transactionId", null);
//                    result.put("error", e.toString());
//                }
//
//                return result;
//            })

            .addNativeCallback("Query_Execute",
                    (args, resultCallback) -> {
                this.db.query_Execute(args.getString("query"),
                        args.isNull("transactionId") ?
                        null : args.getInt("transactionId"),
                        new ABDatabase.VoidResultCallback_ThrowsException() {

                    @Override
                    public void onError(Exception e) {
                        Log.w("ABDataDatabase", e.getMessage(), e);
                        JSONObject result = new JSONObject();

                        try {
                            result.put("error", e.getMessage());
                        } catch (JSONException e_JSON) {
                            Log.e("ABDataDatabase", e_JSON.getMessage(),
                                    e_JSON);
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }

                    @Override
                    public void onResult() {
                        JSONObject result = new JSONObject();

                        try {
                            result.put("error", JSONObject.NULL);
                        } catch (Exception e) {
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }
                });
            })
//            .addNative("Query_Execute", (args) -> {
//                JSONObject result = new JSONObject();
//
//                try {
//                    self.db.query_Execute(args.getString("query"),
//                            args.isNull("transactionId") ?
//                            null : args.getInt("transactionId"));
//
//                    result.put("success", true);
//                    result.put("error", JSONObject.NULL);
//                } catch (ABDatabaseException e) {
//                    result.put("success", false);
//                    result.put("error", e.toString());
//                } catch (SQLException e) {
//                    result.put("success", false);
//                    result.put("error", e.toString());
//                }
//
//                return result;
//            })

            .addNativeCallback("Query_Select", (args, resultCallback) -> {
                JSONArray columnTypes_JSON = args.getJSONArray(
                        "columnTypes");
                String[] columnTypes = new String[columnTypes_JSON.length()];
                for (int i = 0; i < columnTypes.length; i++)
                    columnTypes[i] = columnTypes_JSON.getString(i);

                this.db.query_Select(args.getString("query"), columnTypes,
                        args.isNull("transactionId") ?
                        null : args.getInt("transactionId"),
                        new ABDatabase.SelectResultCallback() {
                    @Override
                    public void onError(Exception e) {
                        Log.w("ABDataDatabase", e.getMessage(), e);

                        JSONObject result = new JSONObject();

                        try {
                            result.put("rows", JSONObject.NULL);
                            result.put("error", e.getMessage());
                        } catch (JSONException e_JSON) {
                            Log.e("ABDataDatabase", e_JSON.getMessage(),
                                    e_JSON);
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }

                    @Override
                    public void onResult(List<JSONArray> rows) {
                        JSONArray rows_JSON = new JSONArray();
                        for (int i = 0; i < rows.size(); i++)
                            rows_JSON.put(rows.get(i));

                        JSONObject result = new JSONObject();

                        try {
                            result.put("rows", rows_JSON);
                            result.put("error", JSONObject.NULL);
                        } catch (Exception e) {
                            resultCallback.onError(e);
                            return;
                        }

                        resultCallback.onResult(result);
                    }
                });

            });
//            .addNative("Query_Select", (args) -> {
//                JSONObject result = new JSONObject();
//
//                JSONArray columnTypes_JSON = args.getJSONArray(
//                        "columnTypes");
//                String[] columnTypes = new String[columnTypes_JSON.length()];
//                for (int i = 0; i < columnTypes.length; i++)
//                    columnTypes[i] = columnTypes_JSON.getString(i);
//
//                try {
//                    List<JSONArray> rows = self.db.query_Select(args.getString(
//                            "query"), columnTypes,
//                            args.isNull("transactionId") ?
//                            null : args.getInt("transactionId"));
//                    JSONArray rows_JSON = new JSONArray();
//                    for (int i = 0; i < rows.size(); i++)
//                        rows_JSON.put(rows.get(i));
//
////                        result.put("success", true);
//                    result.put("rows", rows_JSON);
//                    result.put("error", JSONObject.NULL);
//                } catch (ABDatabaseException e) {
//                    Log.e("ABDatabase", "Query_Select Error", e);
////                        result.put("success", false);
//                    result.put("rows", JSONObject.NULL);
//                    result.put("error", e.getMessage());
//                }
//
//                return result;
//            });

        nativeApp.addActionsSet("ABDatabase", this.nativeActions);
    }

}
