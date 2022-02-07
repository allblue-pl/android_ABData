package pl.allblue.abdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import pl.allblue.abdata.fields.ABDArray;
import pl.allblue.abdata.fields.ABDObject;

@Dao
abstract public class ABDDao<ABDObjectClass extends ABDObject>
{

    @Delete
    abstract public void delete(ABDObjectClass... rows);

    @Insert
    abstract public void insert(ABDObjectClass... rows);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void update(ABDObjectClass... rows);

    abstract public int count();

}
