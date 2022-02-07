package pl.allblue.abdata;

import org.json.JSONException;

public class JSONOperationError extends AssertionError
{

    private JSONException jsonException = null;

    public JSONOperationError(JSONException jsonException)
    {
        this.jsonException = jsonException;
    }

    @Override
    public String getMessage()
    {
        return jsonException.getMessage();
    }

}
