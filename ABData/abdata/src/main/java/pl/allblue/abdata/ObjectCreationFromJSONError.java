package pl.allblue.abdata;

import android.util.Log;

import org.json.JSONException;

public class ObjectCreationFromJSONError extends AssertionError
{

    private Exception exception = null;

    public ObjectCreationFromJSONError(Exception exception)
    {
        this.exception = exception;
    }

    @Override
    public String getMessage()
    {
        return this.exception.getMessage();
    }

}
