package pl.allblue.abdata;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;

public class ABDForm {

    private final HashMap<String, Pair<Enum<FieldType>, Object>> fields;
    private ABDValidator validator;

    public ABDForm(ABDValidator validator) {
        this.validator = validator;

        this.fields = new HashMap<>();
    }

    public void addField_Text(String fieldName, TextInputEditText editText,
            TextInputLayout layout) {
        ABDForm self = this;

        this.fields.put(fieldName, new Pair<>(FieldType.Text, editText));

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String error = self.validator.getFieldError(fieldName,
//                        s.toString());
//                layout.setError(error);
//                layout.setErrorEnabled(error != null);
//            }
//        });
    }

    public void setValidatorInfo(JSONObject validatorInfo) {

    }


    enum FieldType {
        DateTime,
        Integer,
        Text,
    }

}
