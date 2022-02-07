package pl.allblue.abdata;

import org.json.JSONException;

public class JSONTypeError extends AssertionError
{

    private JSONException jsonException = null;

    public JSONTypeError(JSONException jsonException)
    {
        this.jsonException = jsonException;
    }

    @Override
    public String getMessage()
    {
        return jsonException.getMessage();
    }

}
