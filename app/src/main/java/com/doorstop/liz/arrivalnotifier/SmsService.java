package com.doorstop.liz.arrivalnotifier;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by liz on 24/09/14.
 */
public class SmsService extends IntentService {

    public static final String TAG = SmsService.class.getSimpleName();

    public static final String PHONE_NUMBER_KEY = "phoneNumberKey";
    public static final String SMS_MESSAGE_KEY = "smsMessageKey";

    public SmsService() {
        super("SmsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle intentExtras = intent.getExtras();

        if (intentExtras.containsKey(PHONE_NUMBER_KEY) && intentExtras.containsKey(SMS_MESSAGE_KEY)) {
            Log.d(TAG, "Sent Sms phone number: " + intentExtras.getString(PHONE_NUMBER_KEY) + " message: " + intentExtras.getString(SMS_MESSAGE_KEY));
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(intentExtras.getString(PHONE_NUMBER_KEY), null, intentExtras.getString(SMS_MESSAGE_KEY), null, null);
        }

    }
}
