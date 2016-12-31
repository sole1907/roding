package roding.soconcepts.com.roding.util;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Soul on 11/18/2015.
 */
public class ViewUtil {

    public static boolean requiredField(View view) {
        boolean valid = true;

        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            if (editText.getText().toString().length() == 0) {
                editText.setError("This field is required");
                valid = false;
            }
        } else if (view instanceof CheckBox) {
            CheckBox checkbox = (CheckBox) view;
            if (!checkbox.isChecked()) {
                checkbox.setError("This field is required");
                valid = false;
            }
        }

        return valid;
    }

    public static boolean requiredField(View view, String emptyString) {
        boolean valid = true;

        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            if (editText.getText().toString().length() == 0) {
                editText.setError("This field is required");
                valid = false;
            }
        } else if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            if (spinner.getSelectedItem().toString().equals(emptyString)) {
                TextView errorText = (TextView) spinner.getSelectedView();
                errorText.setError("This field is required");
                valid = false;
            }
        }

        return valid;
    }

    public static boolean equalsField(View view1, View view2, String error) {
        boolean valid = true;

        if (view1 instanceof EditText && view2 instanceof EditText) {
            EditText editText1 = (EditText) view1;
            EditText editText2 = (EditText) view2;
            if (!editText1.getText().toString().equals(editText2.getText().toString())) {
                editText2.setError(error);
                valid = false;
            }
        }

        return valid;
    }

    public final static boolean isValidEmailField(View view) {
        boolean valid = true;

        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            valid = android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
            if (!valid) {
                editText.setError("Invalid email address");
            }
        }

        return valid;
    }
}
