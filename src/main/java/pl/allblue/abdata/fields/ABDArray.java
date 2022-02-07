package pl.allblue.abdata.fields;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import pl.allblue.abdata.ABDField;
import pl.allblue.abdata.JSONOperationError;
import pl.allblue.abdata.ObjectCreationError;
import pl.allblue.abdata.ObjectCreationFromJSONError;
import pl.allblue.abdata.UnsupportedABDType;

public class ABDArray<ItemClass extends Object> implements ABDField
{

    private Class<ItemClass> itemClass = null;
    private List<ItemClass> list = new ArrayList<>();

    public ABDArray(Class<ItemClass> itemClass)
    {
        this.itemClass = itemClass;
    }

    public ABDArray(Class<ItemClass> itemClass, JSONArray jsonArray)
    {
        this(itemClass);

        this.setJSON(jsonArray);
    }

    public ABDArray(Class<ItemClass> itemClass, List<ItemClass> list)
    {
        this(itemClass);

        this.setList(list);
    }

    public void add(int index, ItemClass item)
    {
        this.list.add(index, item);
    }

    public void addAll(@NotNull ABDArray<ItemClass> items)
    {
        if (items == null)
            throw new NullPointerException("'items' cannot be null.");

        this.list.addAll(items.getList());
    }

    public void clear()
    {
        this.list.clear();
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

    public ItemClass get(int index)
    {
        return this.list.get(index);
    }

    public JSONArray getJSON()
    {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < this.length(); i++) {
            /* Null */
            if (this.get(i) == null)
                jsonArray.put(null);
            /* Basic Types */
            else if (this.itemClass == Boolean.class)
                jsonArray.put(this.get(i));
            else if (this.itemClass == Double.class)
                jsonArray.put(this.get(i));
            else if (this.itemClass == Integer.class)
                jsonArray.put(this.get(i));
            else if (this.itemClass == Long.class)
                jsonArray.put(this.get(i));
            else if (this.itemClass == String.class)
                jsonArray.put(this.get(i));
            /* ABD Types */
            else if (ABDObject.class.isAssignableFrom(this.itemClass))
                jsonArray.put(((ABDObject)this.get(i)).getJSON());
            else if (ABDArray.class.isAssignableFrom(this.itemClass))
                jsonArray.put(((ABDArray)this.get(i)).getJSON());
            /* Unsupported */
            else
                throw new UnsupportedABDType();
        }

        return jsonArray;
    }

    public List<ItemClass> getList()
    {
        return this.list;
    }

    public int indexOf(ItemClass item)
    {
        return this.list.indexOf(item);
    }

    public int length()
    {
        return this.list.size();
    }

    public ABDArray<ItemClass> orderBy(OrderBy<ItemClass> orderBy)
    {
        throw new AssertionError("Not implemented yet.");
    }

    public ItemClass pop(int index)
    {
        return this.list.remove(index);
    }

    public ItemClass removeAt(int index)
    {
        return this.list.remove(index);
    }

    public void push(ItemClass item)
    {
        this.list.add(item);
    }

    public void set(int index, ItemClass item)
    {
        this.list.set(index, item);
    }

    public void setJSON(JSONArray jsonArray)
    {
        for (int i = 0; i < jsonArray.length(); i++) {
            ItemClass item = this.createItemFromJSON(jsonArray, i);
            this.list.add(item);
        }
    }

    public void setList(List<ItemClass> list)
    {
        this.list.clear();
        this.list.addAll(list);
    }

    public ItemClass[] toArray()
    {
        ItemClass[] array = (ItemClass[])Array.newInstance(this.itemClass, this.length());
        for (int i = 0; i < array.length; i++)
            array[i] = this.get(i);

        return array;
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


    private ABDArray<ItemClass> create()
    {
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

    private ItemClass createItemFromJSON(JSONArray jsonArray, int index)
    {
        if (jsonArray.isNull(index))
            return null;

        try {
            if (ABDArray.class.isAssignableFrom(this.itemClass))
                throw new AssertionError("Array item class not provided.");
            if (this.itemClass == Boolean.class)
                return (ItemClass)(Boolean)jsonArray.getBoolean(index);
            if (this.itemClass == Double.class)
                return (ItemClass)(Double)jsonArray.getDouble(index);
            if (this.itemClass == Integer.class)
                return (ItemClass)(Integer)jsonArray.getInt(index);
            if (this.itemClass == Long.class)
                return (ItemClass)(Long)jsonArray.getLong(index);
            if (this.itemClass == String.class)
                return (ItemClass)(String)jsonArray.getString(index);
            if (ABDObject.class.isAssignableFrom(this.itemClass)) {
                try {
                    JSONObject itemJSON = jsonArray.getJSONObject(index);

                    return this.itemClass.getConstructor(JSONObject.class)
                            .newInstance(itemJSON);
                } catch (IllegalAccessException e) {
                    throw new ObjectCreationFromJSONError(e);
                } catch (InstantiationException e) {
                    throw new ObjectCreationFromJSONError(e);
                } catch (InvocationTargetException e) {
                    throw new ObjectCreationFromJSONError(e);
                } catch (NoSuchMethodException e) {
                    throw new ObjectCreationFromJSONError(e);
                }
            }

            throw new UnsupportedABDType();
        } catch(JSONException e){
            throw new JSONOperationError(e);
        }
    }


    public interface OrderBy<ItemClass>
    {
        int compares(ItemClass itemA, ItemClass itemB);
    }

    public interface Where<ItemClass>
    {
        boolean matches(ItemClass item);
    }

}
