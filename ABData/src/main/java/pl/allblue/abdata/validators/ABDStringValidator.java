package pl.allblue.abdata.validators;

import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;

import pl.allblue.ablibs.strings.ABStrings;

public class ABDStringValidator extends ABDFieldValidator {

    static public class Properties extends
            ABDFieldValidator.Properties<Properties>  {

        public boolean required;
        public Integer minLength;
        public Integer maxLength;
        public RegExpProperty regexp;
        public String chars;

        public Properties() {
            this.required = true;
            this.minLength = null;
            this.maxLength = null;
            this.regexp = null;
            this.chars = ABStrings.getCharsRegexp_Basic();
        }

        public Properties(JSONObject propertiesJSON) {
            try {
                if (propertiesJSON.has("required"))
                    this.required = propertiesJSON.getBoolean("required");
                if (propertiesJSON.has("minLength")) {
                    this.minLength = propertiesJSON.isNull("minLength") ?
                            null : propertiesJSON.getInt("minLength");
                }
                if (propertiesJSON.has("maxLength")) {
                    this.maxLength = propertiesJSON.isNull("maxLength") ?
                            null : propertiesJSON.getInt("maxLength");
                }
                if (propertiesJSON.has("regexp")) {
                    if (propertiesJSON.isNull("regexp")) {
                        this.regexp = null;
                    } else {
                        RegExpProperty regexp = new RegExpProperty(null, null);
                        JSONArray regexpArr = propertiesJSON.getJSONArray(
                                "regexp");
                        regexp.regexp = regexpArr.getString(0);
                        regexp.errorMessage = regexpArr.getString(1);
                    }
                }
                if (propertiesJSON.has("chars")) {
                    this.chars = propertiesJSON.isNull("chars") ?
                            null : propertiesJSON.getString("chars");
                }
            } catch (JSONException e) {
                Log.e("ABDStringValidator",
                        "Cannot parse JSON properties.", e);
            }
        }

        public Properties setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Properties setRegexp(String regexp, String errorMessage) {
            this.regexp = new RegExpProperty(regexp, errorMessage);
            return this;
        }


        public class RegExpProperty {
            public String regexp;
            public String errorMessage;

            public RegExpProperty(String regexp, String errorMesssage) {
                this.regexp = regexp;
                this.errorMessage = errorMesssage;
            }
        }

    }


    private Properties properties;

    public ABDStringValidator(Properties properties) {
        super(properties);

        this.properties = properties;
    }


    @Override
    protected String _getError(Object value) {
        Log.d("ABDStringValidator", "Testing _getError");

        return "Test error.";
    }

}