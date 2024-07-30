package pl.allblue.abdata.validators;

import android.content.res.Resources;

import pl.allblue.abdata.ABDValidator;
import pl.allblue.abdata.R;

public class ABDBoolValidator extends ABDFieldValidator {

    static public class Properties extends
            ABDFieldValidator.Properties<ABDStringValidator.Properties> {

        public Properties() {
            super();
        }

    }


    ABDBoolValidator(Properties properties) {
        super(properties);
    }


    @Override
    protected String _getError(Object rawValue) {
        if (!(rawValue instanceof Boolean))
            return Resources.getSystem().getString(R.string.wrong_type);

        Boolean value = (Boolean)rawValue;
        String error = super.getError(value);
        if (value != null) {
            if (!value) {
                if (this.properties.required)
                    return Resources.getSystem().getString(R.string.not_checked);
            }
        }

        return null;
    }

}
