package com.galp.plugins.ar;

import android.content.Intent;
import android.hardware.Camera;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class GalpArImpl extends CordovaPlugin {

    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    static private CallbackContext _callbackContext;

    public static final int BACK_CAMERA = 1;
    public static final int FRONT_CAMERA = 2;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        _callbackContext = callbackContext;

        if (action.equals("scan")) {
            this.scan();
            return true;
        }
        return false;
    }

    private void scan() {
        Intent myIntent = new Intent( this.cordova.getContext(), CustomArActivity.class);
        this.cordova.startActivityForResult(null, myIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (_callbackContext != null) {
            if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
    
            IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

            if(result.getContents() == null) {
                Intent originalIntent = result.getOriginalIntent();

                try {
                    JSONObject jsonResult = new JSONObject();
                    if (originalIntent == null) {
                        jsonResult.put("code", "OS-PLUG-BARC-0006");
                        jsonResult.put("message", "Scanning cancelled.");
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        jsonResult.put("code", "OS-PLUG-BARC-0007");
                        jsonResult.put("message", "Scanning cancelled due to missing camera permissions.");
                    }
                    _callbackContext.error(jsonResult);

                } catch (Exception e) {
                    _callbackContext.error("Cancelled");
                }

            } else {
                _callbackContext.success(result.getContents());
            }
        }
    }

}