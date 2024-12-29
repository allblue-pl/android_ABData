package pl.allblue.abdata.validators;

import android.content.res.Resources;

import pl.allblue.abdata.ABDValidator;
import pl.allblue.abdata.R;

abstract public class ABDFieldValidator {

    protected static class Properties<PropertiesClass extends Properties> {

        boolean notNull;
        boolean required;

        Properties() {
            this.notNull = true;
            this.required = true;
        }

        @SuppressWarnings("unchecked")
        public PropertiesClass setNotNull(boolean notNull) {
            this.notNull = notNull;
            return (PropertiesClass)this;
        }

        @SuppressWarnings("unchecked")
        public PropertiesClass setRequired(boolean required) {
            this.required = required;
            return (PropertiesClass)this;
        }

    }


    protected Properties properties;


    public ABDFieldValidator(Properties properties) {
        this.properties = properties;
    }

    public String getError(Object value) {
        if (value == null) {
            if (this.properties.notNull) {
                if (this.properties.required)
                    return Resources.getSystem().getString(R.string.not_set);
                else
                    return Resources.getSystem().getString(R.string.not_null);
            }
        }

        return this._getError(value);
    }

    public boolean validate(ABDValidator validator, String fieldName,
            Object value) {
        String errorMessage = this.getError(value);
        if (errorMessage == null)
            return true;

        validator.fieldError(fieldName, errorMessage);
        return false;
    }


    abstract protected String _getError(Object value);

}
