package pl.allblue.abdata.fields;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import pl.allblue.abdata.ABDField;
import pl.allblue.abdata.JSONOperationError;

public class ABDArray<ItemClass extends Object> extends ABDField
{

    Class<ItemClass> itemClass = null;
    JSONArray json = null;

    public ABDArray(Class<ItemClass> itemClass, JSONArray jsonArray)
    {
        if (!ABDArray.class.isAssignableFrom(itemClass) &&
                itemClass != Boolean.class &&
                itemClass != Integer.class &&
                itemClass != Long.class &&
                !ABDObject.class.isAssignableFrom(itemClass) &&
                itemClass != String.class)
            throw new AssertionError("Unsupported ABD type.");

        if (jsonArray == null)
            throw new AssertionError("`jsonArray` cannot be null.");

        this.itemClass = itemClass;
        this.json = jsonArray;
    }

    public ABDArray(Class<ItemClass> itemClass)
    {
        this(itemClass, new JSONArray());
    }

    public ItemClass get(int index)
    {
        if (index < 0 || index >= this.json.length())
            throw new IndexOutOfBoundsException();

        try {
            if (this.json.isNull(index))
                return null;

            if (ABDArray.class.isAssignableFrom(this.itemClass))
                throw new AssertionError("Array item class not provided.");
            if (this.itemClass == Boolean.class)
                return (ItemClass)(Boolean)this.json.getBoolean(index);
            if (this.itemClass == Integer.class)
                return (ItemClass)(Integer)this.json.getInt(index);
            if (this.itemClass == Long.class)
                return (ItemClass)(Long)this.json.getLong(index);
            if (ABDObject.class.isAssignableFrom(this.itemClass))
                return (ItemClass)(new ABDObject(this.json.getJSONObject(index)));
            if (this.itemClass == String.class)
                return (ItemClass)this.json.getString(index);

            throw new AssertionError("Unsupported ABD type.");
        } catch (JSONException e) {
            throw new JSONOperationError(e);
        }
    }

    public ItemClass get(Class<ItemClass> itemClass, int index)
    {
        if (index < 0 || index >= this.json.length())
            throw new IndexOutOfBoundsException();

        try {
            if (this.json.isNull(index))
                return null;

            if (this.itemClass == ABDArray.class) {
                return (ItemClass) (new ABDArray<ItemClass>(itemClass,
                        this.json.getJSONArray(index)));
            }

            throw new AssertionError("Array item class provided for different item type.");
        } catch (JSONException e) {
            throw new JSONOperationError(e);
        }
    }

    public Class<ItemClass> item()
    {
        return this.itemClass;
    }

    public int length()
    {
        return this.json.length();
    }

    public ItemClass pop()
    {
        return this.get(this.length() - 1);
    }

    public void push(ItemClass item)
    {
        this.set(this.length(), item);
    }

    public JSONArray ref()
    {
        return this.json;
    }

    public void set(int index, ItemClass item)
    {
        try {
            if (item == null)
                this.json.put(index, null);
            else if (ABDArray.class.isAssignableFrom(this.itemClass))
                this.json.put(index, ((ABDArray)item).ref());
            else if (this.itemClass == Boolean.class)
                this.json.put(index, (Boolean)item);
            else if(this.itemClass == Integer.class)
                this.json.put(index, (Integer)item);
            else if(this.itemClass == Long.class)
                this.json.put(index, (Long)item);
            else if (ABDObject.class.isAssignableFrom(this.itemClass))
                this.json.put(index, ((ABDObject)item).ref());
            else if (this.itemClass == String.class)
                this.json.put(index, (String)item);
            else {
                throw new AssertionError("Unsupported ABD type: " +
                        this.itemClass.toString());
            }
        } catch (JSONException e) {
            throw new JSONOperationError(e);
        }
    }

}
