package pl.allblue.abdata.validators;

public class ABDLongValidator extends ABDFieldValidator {

    static public class Properties extends
            ABDFieldValidator.Properties<Properties> {

        public Properties() {
            super();
        }

    }


    public ABDLongValidator(Properties properties) {
        super(properties);
    }


    @Override
    protected String _getError(Object value) {
        return "";
    }
}
