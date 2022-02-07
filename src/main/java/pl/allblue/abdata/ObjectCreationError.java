package pl.allblue.abdata;

public class ObjectCreationError extends AssertionError
{

    private Exception exception = null;

    public ObjectCreationError(Exception exception)
    {
        this.exception = exception;
    }

    @Override
    public String getMessage()
    {
        return exception.getMessage();
    }

}
