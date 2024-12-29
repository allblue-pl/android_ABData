package pl.allblue.abdata;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper
{

    static public List<JSONObject> GetListFromData(JSONObject jsonData,
            String propertyName) {
        try {
            if (!jsonData.has(propertyName))
                return new ArrayList<>();

            JSONArray rows_JSON = jsonData.getJSONArray(propertyName);
            List<JSONObject> jsonProperty_List = new ArrayList<>();
            for (int i = 0; i < rows_JSON.length(); i++)
                jsonProperty_List.add(rows_JSON.getJSONObject(i));

            return jsonProperty_List;
        } catch (JSONException e) {
            Log.e("Logs", "JSONException", e);
            return null;
        }
    }

    static public JSONObject GetObjectFromData(JSONObject jsonData,
            String propertyName) {
        try {
            if (!jsonData.has(propertyName))
                return new JSONObject();

            return jsonData.getJSONObject(propertyName);
        } catch (JSONException e) {
            Log.e("Logs", "JSONException", e);
            return null;
        }
    }

    static public boolean SaveFile(Context context, String filePath,
            JSONObject json) {
        File file = new File(context.getFilesDir(), filePath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] data_bytes = json.toString().getBytes();
            fos.write(data_bytes);
            fos.close();
        } catch (IOException e) {
            Log.e("Logs", "IOException", e);
            return false;
        }

        return true;
    }

    static public JSONObject LoadFile(Context context, String filePath) {
        File file = new File(context.getFilesDir(), filePath);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();

                String json_String = new String(data, "UTF-8");
                JSONObject jsonData = new JSONObject((json_String));

                return jsonData;
            } catch (IOException e) {
                Log.e("Logs", "IOException", e);
                return null;
            } catch (JSONException e) {
                Log.e("Logs", "JSONException", e);
                return null;
            }
        }

        return new JSONObject();
    }

}
