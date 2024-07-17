package pl.allblue.abdata.validators;

import android.util.Pair;

public class ABDStringValidator {

    class Properties extends DefaultProperties  {

        public boolean required;
        public Integer minLength;
        public Integer maxLength;
        public Pair<String, String> regexp;
        public String chars;

        public Properties() {
            this.required = true;
            this.minLength = null;
            this.maxLength = null;
            this.regexp = null;
            this.chars = ABStrings.getCharsRegExp_Basic();
        }

    }

}