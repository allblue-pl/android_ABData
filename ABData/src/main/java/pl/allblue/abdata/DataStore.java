package pl.allblue.abdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import pl.allblue.abdatabase.ABDatabase;

public class DataStore {

    static public void GetDBSchemeVersion(ABDatabase db, Integer transactionId,
            OnGetDBSchemeVersion callback) {
        Integer version = null;
        db.query_Select("SELECT Name, Data FROM _ABData_Settings" +
                " WHERE Name = 'version'", new String[] {"String", "JSON" },
                transactionId, new ABDatabase.SelectResultCallback() {
            @Override
            public void onError(Exception e) {
                Log.d("DataStore", "Cannot get DB scheme version.", e);
                callback.onGetDBSchemeVersion(null);
            }

            @Override
            public void onResult(List<JSONArray> rows) {
                if (rows.size() == 0) {
                    callback.onGetDBSchemeVersion(null);
                    return;
                }

                try {
                    callback.onGetDBSchemeVersion(rows.get(0).getInt(1));
                } catch (JSONException e) {
                    Log.d("DataStore", "Cannot get DB scheme version.", e);
                    callback.onGetDBSchemeVersion(null);
                }
            }
        });


    }


    public interface OnGetDBSchemeVersion {
        void onGetDBSchemeVersion(Integer dbSchemeVer);
    }

}
