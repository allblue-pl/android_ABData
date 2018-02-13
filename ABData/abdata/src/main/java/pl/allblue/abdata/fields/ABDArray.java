package pl.allblue.abdata.fields;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import pl.allblue.abdata.ABDField;
import pl.allblue.abdata.JSONOperationError;
import pl.allblue.abdata.JSONTypeError;
import pl.allblue.abdata.ObjectCreationError;
import pl.allblue.abdata.ObjectCreationFromJSONError;

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

    public ItemClass first()
    {
        if (this.length() == 0)
            return null;

        return this.get(0);
    }

    public ItemClass first_Where(Where<ItemClass> where)
    {
        for (int i = 0; i < this.length(); i++) {
            ItemClass item = this.get(i);
            if (where.matches(item))
                return item;
        }

        return null;
    }

    public ItemClass firstArray(Class<ItemClass> itemClass)
    {
        if (this.length() == 0)
            return null;

        return this.getArray(itemClass, 0);
    }

    public ItemClass firstArray_Where(Class<ItemClass> itemClass)
    {
        throw new AssertionError("Not implemented yet.");
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
            if (ABDObject.class.isAssignableFrom(this.itemClass)) {
                try {
                    Class<?> enclosingClass = this.itemClass.getEnclosingClass();
                    JSONObject json = this.json.getJSONObject(index);

                    if (enclosingClass == null) {
                        return (ItemClass)this.itemClass.getConstructors()[1]
                                .newInstance(json);
                    } else {
                        return (ItemClass)this.itemClass.getConstructors()[1]
                                .newInstance(this, json);
                    }
                } catch (IllegalAccessException e) {
                    throw new ObjectCreationFromJSONError(e);
                } catch (InstantiationException e) {
                    throw new ObjectCreationFromJSONError(e);
                } catch (InvocationTargetException e) {
                    throw new ObjectCreationFromJSONError(e);
                }
            }
            if (this.itemClass == String.class)
                return (ItemClass)this.json.getString(index);

            throw new AssertionError("Unsupported ABD type.");
        } catch (JSONException e) {
            throw new JSONOperationError(e);
        }
    }

    public ItemClass getArray(Class<ItemClass> itemClass, int index)
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

    public void insert(int index, ItemClass item)
    {
        for (int i = this.length() - 1; i >= index; i--)
            this.set(i + 1, this.get(i));

        this.set(index, item);
    }

    public Class<ItemClass> item()
    {
        return this.itemClass;
    }

    public int length()
    {
        return this.json.length();
    }

    public ABDArray<ItemClass> orderBy(OrderBy<ItemClass> orderBy)
    {
        throw new AssertionError("Not implemented yet.");
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

    public void remove(int index)
    {
        if (index < 0 || index >= this.json.length())
            throw new IndexOutOfBoundsException();

        try {
            Field valuesField = JSONArray.class.getDeclaredField("values");
            valuesField.setAccessible(true);
            List<Object> values = (List<Object>) valuesField.get(this.json);
            if (index >= values.size())
                return;
            values.remove(index);
        } catch (Exception e) {
            throw new AssertionError("Temporary solution.");
        }

//        try {
//            for (int i = index; i < this.length() - 1; i++)
//                this.json.put(i, this.get(i + 1));
//
//            this.json.remove(this.length() - 1);
//        } catch (JSONException e) {
//            throw new JSONOperationError(e);
//        }
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

    public ABDArray<ItemClass> where(Where<ItemClass> where)
    {
        ABDArray<ItemClass> items = this.create();

        for (int i = 0; i < this.length(); i++) {
            ItemClass item = this.get(i);
            if (where.matches(item))
                items.push(item);
        }

        return items;
    }


    public interface OrderBy<ItemClass>
    {
        int compares(ItemClass itemA, ItemClass itemB);
    }

    public interface Where<ItemClass>
    {
        boolean matches(ItemClass item);
    }


    private ABDArray<ItemClass> create()
    {
        Class<?> enclosingClass = this.getClass().getEnclosingClass();

        try {
            return (ABDArray<ItemClass>)this.getClass().getConstructors()[0]
                    .newInstance(this.itemClass);
        } catch (IllegalAccessException e) {
            throw new ObjectCreationError(e);
        } catch (InstantiationException e) {
            throw new ObjectCreationError(e);
        } catch (InvocationTargetException e) {
            throw new ObjectCreationError(e);
        }
    }

}
