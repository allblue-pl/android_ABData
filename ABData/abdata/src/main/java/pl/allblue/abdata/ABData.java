package pl.allblue.abdata;

import android.util.Pair;

import java.util.List;

public class ABData
{

    static public Id CreateId()
    {
        return new Id();
    }


    static public class Id
    {

        private long id = 0l;
        private int currentBit = 0;

        public Id()
        {

        }

        public Id add(Integer part, int size)
        {
            for (int i = currentBit; i < this.currentBit + size; i++)
                this.id |= 1 << i & part;

            return this;
        }

        public long getLong()
        {
            return this.id;
        }

        public String getString()
        {
            return Long.toBinaryString((long)this.id);
        }

    }

}
