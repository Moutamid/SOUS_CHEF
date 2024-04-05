package com.moutamid.souschef;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.souschef.models.GroceryModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Constants {

    static Dialog dialog;
    public static final String USER = "USER";
    public static final String NOTIFICATIONS = "NOTIFICATIONS";
    public static final String STASH_USER = "STASH_USER";
    public static final String WEEK_MEAL = "WEEK_MEAL";
    public static final String GROCERY = "GROCERY";
    public static final String PANTRY = "PANTRY";
    public static final String SUGGESTED_MEAL = "SUGGESTED_MEAL";

    public static void initDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog() {
        dialog.show();
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static double addQuantities(String quantity1, String quantity2) {
        // Extract numeric values
        double value1 = parseQuantity(quantity1);
        double value2 = parseQuantity(quantity2);

        // Add the values and return the result
        return value1 + value2;
    }

    public static double parseQuantity(String quantity) {
        String[] parts = quantity.split(" ");
        if (parts.length > 1) {
            String[] fractionParts = parts[0].split("/");
            if (fractionParts.length == 2) {
                double numerator = Double.parseDouble(fractionParts[0]);
                double denominator = Double.parseDouble(fractionParts[1]);
                return numerator / denominator;
            } else {
                try {
                    return Double.parseDouble(parts[0]);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        } else {
            try {
                return Double.parseDouble(parts[0]);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    public static String getUnit(ArrayList<GroceryModel> list, String itemName) {
        try {
            for (GroceryModel item : list) {
                if (item.ingredient.equals(itemName)) {
                    if (!item.quantity.split(" ")[1].isEmpty()) {
                        return item.quantity.split(" ")[1];
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return "";
    }


    public static void checkApp(Activity activity) {
        String appName = "souschef";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }
    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("souschef");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference() {
        return FirebaseStorage.getInstance().getReference().child("souschef");
    }

}
