package pl.allblue.abdata;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.allblue.abdata.validators.ABDFieldValidator;

public class ABDValidator {

    static public ABDValidator CreateFromJSON(JSONObject validatorJSON) {
        ABDValidator v = new ABDValidator();

        Iterator<String> keys =  validatorJSON.keys();
        while (keys.hasNext()) {
            String fieldName = keys.next();

            if (!v.hasField(fieldName))
                v.addField(fieldName);
        }

        return v;
    }


    private Map<String, List<ABDFieldValidator>> fields;

    public ABDValidator() {
        this.fields = new HashMap<>();
    }

    public void addField(String fieldName) {
        this.fields.put(fieldName, new ArrayList<>());
    }

    public void addFieldValidator(String fieldName,
            ABDFieldValidator fieldValidator) {
        if (!this.fields.containsKey(fieldName)) {
            throw new AssertionError("Validator field '" + fieldName +
                    "' does not exist.");
        }

        this.fields.get(fieldName).add(fieldValidator);
    }

    public List<ABDFieldValidator> getFieldValidators(String fieldName) {
        if (!this.fields.containsKey(fieldName)) {
            throw new AssertionError("Validator field '" + fieldName +
                    "' does not exist.");
        }

        return this.fields.get(fieldName);
    }

    public String getFieldError(String fieldName, String value) {
        Iterator<ABDFieldValidator> i = this.getFieldValidators(fieldName).iterator();
        while (i.hasNext()) {
            ABDFieldValidator fieldValidator = i.next();
            String error = fieldValidator.getError(value);
            if (error != null)
                return error;
        }

        return null;
    }

    public void fieldError(String fieldName, String fieldError) {

    }

    public boolean hasField(String fieldName) {
        return this.fields.containsKey(fieldName);
    }

}
