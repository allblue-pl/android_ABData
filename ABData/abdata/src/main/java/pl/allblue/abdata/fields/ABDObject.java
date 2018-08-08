package pl.allblue.abdata.fields;

import android.arch.persistence.room.RoomDatabase;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import pl.allblue.abdata.ABDField;
import pl.allblue.abdata.JSONOperationError;
import pl.allblue.abdata.JSONTypeError;
import pl.allblue.abdata.ObjectCreationFromJSONError;

public class ABDObject implements ABDField
{

    public ABDObject()
    {

    }

    public JSONObject getJSON()
    {
        JSONObject json = new JSONObject();

        try {
            this.__getJSON(json);
        } catch (JSONException e) {
            throw new AssertionError("Cannot parse json.");
        }

        return json;
    }

    public void setJSON(JSONObject json)
    {
        try {
            this.__setJSON(json);
        } catch (JSONException e) {
            throw new AssertionError("Cannot parse json.");
        }
    }


//    public <ItemClass extends ABDField> ABDArray<ItemClass> getArray(
//            Class<ItemClass> itemClass, String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return new ABDArray<ItemClass>(itemClass, this.json.getJSONArray(fieldName));
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public ABDObject()
//    {
//        this(new JSONObject());
//    }
//
//    public Boolean getBool(String fieldName)
//    public Boolean getBool(String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return this.json.getBoolean(fieldName);
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public byte[] getData(String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return Base64.decode(this.json.getString(fieldName), 0);
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public Integer getInt(String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return this.json.getInt(fieldName);
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public Double getDouble(String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return this.json.getDouble(fieldName);
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public Long getLong(String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return this.json.getLong(fieldName);
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public <ItemClass extends ABDObject> ItemClass getObject(
//            Class<ItemClass> fieldClass, String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            try {
//                Constructor<?> constructor = fieldClass.getConstructors()[1];
//                Class<?> enclosingClass = fieldClass.getEnclosingClass();
//                JSONObject json = this.json.getJSONObject(fieldName);
//
//                if (enclosingClass == null) {
//                    return (ItemClass) fieldClass.getConstructors()[1]
//                            .newInstance(json);
//                } else {
//                    return (ItemClass) fieldClass.getConstructors()[1]
//                            .newInstance(this, json);
//                }
//            } catch (IllegalAccessException e) {
//                throw new ObjectCreationFromJSONError(e);
//            } catch (InstantiationException e) {
//                throw new ObjectCreationFromJSONError(e);
//            } catch (InvocationTargetException e) {
//                throw new ObjectCreationFromJSONError(e);
//            }
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }
//
//    public String getString(String fieldName)
//    {
//        if (!this.json.has(fieldName))
//            return null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            return this.json.getString(fieldName);
//        } catch (JSONException e) {
//            throw new JSONTypeError(e);
//        }
//    }

//    public String getString(String fieldName)
//    {
//        Object value = null;
//
//        if (this.json.isNull(fieldName))
//            return null;
//
//        try {
//            value = this.json.get(fieldName);
//        } catch (JSONException e) {
//            return "";
//        }
//
//        if (value instanceof String)
//            return (String)value;
//
//        if (value instanceof Boolean)
//            return (Boolean)value ? "true" : "false";
//        if (value instanceof Integer)
//            return Integer.toString((int)value);
//        if (value instanceof Long)
//            return Long.toString((long)value);
//        if (value instanceof Double)
//            return Double.toString((double)value);
////        if (value instanceof String)
////            return value;
//        if (value instanceof JSONArray)
//            return "Array";
//
//        throw new UnknownJSONTypeError();
//    }

//    public boolean isSet(String fieldName)
//    {
//        return this.json.has(fieldName);
//    }
//
//    public JSONObject ref()
//    {
//        return this.json;
//    }

//    public void setArray(String fieldName, ABDArray fieldValue)
//    {
//        try {
//            this.json.put(fieldName, fieldValue == null ? null : fieldValue.json);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }
//
//    public void setBool(String fieldName, Boolean fieldValue)
//    {
//        try {
//            this.json.put(fieldName, fieldValue);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }
//
//    public void setData(String fieldName, byte[] data)
//    {
//        try {
//            this.json.put(fieldName, Base64.encode(data, 0));
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }
//
//    public void setInt(String fieldName, Integer fieldValue)
//    {
//        try {
//            this.json.put(fieldName, fieldValue);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }
//
//    public void setLong(String fieldName, Long fieldValue)
//    {
//        try {
//            this.json.put(fieldName, fieldValue);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }
//
//    public void setObject(String fieldName, ABDObject fieldValue)
//    {
//        try {
//            this.json.put(fieldName, fieldValue == null ? null : fieldValue.json);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }
//
//    public void setString(String fieldName, String fieldValue)
//    {
//        try {
//            this.json.put(fieldName, fieldValue);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
//    }


    /* Object Overrides */
//    @Override
//    public boolean equals(Object obj)
//    {
//        return ((ABDObject)obj).ref() == this.ref();
//    }
    /* / Object Overrides */


    protected void __getJSON(JSONObject json) throws JSONException
    {

    }
    protected void __setJSON(JSONObject json) throws JSONException
    {

    }

}
