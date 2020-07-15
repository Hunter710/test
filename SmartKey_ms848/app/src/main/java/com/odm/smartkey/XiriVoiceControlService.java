package com.odm.smartkey;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.iflytek.xiri.AppService;
import com.iflytek.xiri.Feedback;


///key word: XiriService
public class XiriVoiceControlService extends AppService {
    private static final String strTAG = "com.odm.smartkey." + XiriVoiceControlService.class.getSimpleName();
    private Context objContext;
    private Feedback objFeedback = null;
    
    private IAppListener mAppListener = new IAppListener() {
        @SuppressWarnings("deprecation")
        @Override
        public void onExecute(Intent intent) {
            objFeedback.begin(intent);
            String strCmd = intent.getStringExtra("_command");
            Log.v(strTAG, "Cmd:" + strCmd + " onExecute " + Uri.decode(intent.toURI()));
            if (strCmd.equals("open")) {
                ///open live tv activity
                Log.v(strTAG, "open smartkey activity");
                intent.setClassName("com.odm.smartkey", "com.odm.smartkey.SmartKeyActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                objFeedback.feedback("????????????", Feedback.DIALOG);
            } else if (strCmd.equals("close")) {
                ///close live tv activity
                Log.v(strTAG, "close smartkey activity, start home luncher");
                Intent intentLuncher = new Intent(Intent.ACTION_MAIN);
                intentLuncher.addCategory(Intent.CATEGORY_HOME);
                intentLuncher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLuncher);
                objFeedback.feedback("?????????????", Feedback.DIALOG);
            } else if (strCmd.equals("other")) {
                String strFuzzyWordsCmd = intent.getStringExtra("w_other");
                Log.v(TAG, "w_other:" + strFuzzyWordsCmd);
                String target = null;

                if ((strFuzzyWordsCmd.equals("???")) ||
                        (strFuzzyWordsCmd.equals("???????"))||
                        (strFuzzyWordsCmd.equals("?????"))||
                        (strFuzzyWordsCmd.equals("?????????"))||
                        (strFuzzyWordsCmd.equals("?????"))||
                        (strFuzzyWordsCmd.equals("?????????"))||
                        (strFuzzyWordsCmd.equals("???????"))||
                        (strFuzzyWordsCmd.equals("???????????"))) {
                    objFeedback.feedback("???????", Feedback.DIALOG);
                    target = "sleep";
                    intent.putExtra("_target", target);
                } else if ((strFuzzyWordsCmd.equals("??????")) ||
                        (strFuzzyWordsCmd.equals("??????????"))||
                        (strFuzzyWordsCmd.equals("???????"))||
                        (strFuzzyWordsCmd.equals("???????????"))) {
                    objFeedback.feedback("???????", Feedback.DIALOG);
                    target = "listen";
                    intent.putExtra("_target", target);
                } else if ((strFuzzyWordsCmd.equals("???????")) ||
                        (strFuzzyWordsCmd.equals("???????????"))||
                        (strFuzzyWordsCmd.equals("?????????"))||
                        (strFuzzyWordsCmd.equals("?????????????"))) {
                    objFeedback.feedback("?????????", Feedback.DIALOG);
                    target = "clock";
                    intent.putExtra("_target", target);
                } else if ((strFuzzyWordsCmd.equals("?????")) ||
                        (strFuzzyWordsCmd.equals("?????????"))||
                        (strFuzzyWordsCmd.equals("???????"))||
                        (strFuzzyWordsCmd.equals("???????????"))) {
                    objFeedback.feedback("???????", Feedback.DIALOG);
                    target = "soundbar";
                    intent.putExtra("_target", target);
                } else {
                    objFeedback.feedback("????????????", Feedback.DIALOG);
                }

                ///open live tv activity
                Log.v(strTAG, "open smartkey activity");
                intent.setClassName("com.odm.smartkey", "com.odm.smartkey.SmartKeyActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                objFeedback.feedback("?????????:" + strCmd, Feedback.DIALOG);
            }
        }

        @Override
        public void onTextFilter(Intent var1) {
            Log.v(strTAG, "onTextFilter, not implent");
        }
    };

    @Override
    protected void onInit() {
    }

    @Override
    public void onCreate() {
        Log.v(strTAG, "start voice control service");
        super.onCreate();
        setAppListener(mAppListener);
        objContext = this;
        objFeedback = new Feedback(objContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
