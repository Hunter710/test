package com.odm.smartkey;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class XiriVoiceControlHelper {
    private Map<String, String> mapFuzzyWordsCmdList = null;
    private Map<String, Integer> mapSelectChannelCmdList = null;
    private Map<String, String> mapCmdList;
    private String strTAG;
    private Context objContext;

    public XiriVoiceControlHelper(Context context) {
        mapCmdList = new HashMap<String, String>();
        objContext = context;
        strTAG = objContext.getClass().getName();
    }

    public void SetFuzzyWord(String strCmd, String strFuzzyWord, String strParam) {
        mapCmdList.put(strFuzzyWord, strCmd);

        if (null == mapFuzzyWordsCmdList) {
            mapFuzzyWordsCmdList = new HashMap<String, String>();
        }

        mapFuzzyWordsCmdList.put(strFuzzyWord, strParam);
        Log.v(strTAG, "SetFuzzyWord ==> strCmd:" + strCmd + " strFuzzyWord:" + strFuzzyWord + " strParam:" + strParam);
    }

    public void SetFuzzyWord(String strCmd, String strFuzzyWord, Integer iParam) {
        mapCmdList.put(strFuzzyWord, strCmd);

        if (null == mapSelectChannelCmdList) {
            mapSelectChannelCmdList = new HashMap<String, Integer>();
        }

        mapSelectChannelCmdList.put(strFuzzyWord, iParam);
        Log.v(strTAG, "SetFuzzyWord ==> strCmd:" + strCmd + " strFuzzyWord:" + strFuzzyWord + " iParam:" + iParam);
    }

    public void ResetAllFuzzyWord() {
        mapCmdList.clear();

        if (null != mapFuzzyWordsCmdList) {
            mapFuzzyWordsCmdList.clear();
        }

        if (null != mapSelectChannelCmdList) {
            mapSelectChannelCmdList.clear();
        }

        Log.v(strTAG, "ResetAllFuzzyWord");
    }

    public String GetCmd(Intent intent) {
        if (!intent.hasExtra("_command")) {
            Log.v(strTAG, "ennnn, can't find _command");
            return null;
        }

        String strCmd = intent.getStringExtra("_command");
        Log.v(strTAG, "onExecute:" + strCmd);
        if (!strCmd.equals("fuzzy_command")) {
            Log.v(strTAG, "ennnn, can't find fuzzy_command");
            return null;
        }

        String strFuzzyWordsCmd = GetFuzzyWord(intent);
        Log.v(strTAG, "w_fuzzy_command:" + strFuzzyWordsCmd);

        if (!mapCmdList.containsKey(strFuzzyWordsCmd)) {
            Log.v(strTAG, "ennnn, can't find w_fuzzy_command");
            return null;
        }

        return mapCmdList.get(strFuzzyWordsCmd);
    }

    public String GetFuzzyWord(Intent intent) {
        String strFuzzyWordsCmd = intent.getStringExtra("w_fuzzy_command");
        Log.v(strTAG, "w_fuzzy_command:" + strFuzzyWordsCmd);

        return strFuzzyWordsCmd;
    }

    public String GetStringParam(Intent intent) {
        String strFuzzyWordsCmd = GetFuzzyWord(intent);
        if (!mapFuzzyWordsCmdList.containsKey(strFuzzyWordsCmd)) {
            Log.v(strTAG, "ennnn, can't find string w_fuzzy_command");
            return null;
        }

        Log.v(strTAG, "GetStringParam -> strFuzzyWordsCmd:" + strFuzzyWordsCmd);
        return mapFuzzyWordsCmdList.get(strFuzzyWordsCmd);
    }

    public Integer GetIntegerParam(Intent intent) {
        String strFuzzyWordsCmd = GetFuzzyWord(intent);
        if (!mapSelectChannelCmdList.containsKey(strFuzzyWordsCmd)) {
            Log.v(strTAG, "ennnn, can't find int w_fuzzy_command");
            return null;
        }

        Log.v(strTAG, "GetIntegerParam -> strFuzzyWordsCmd:" + strFuzzyWordsCmd);
        return mapSelectChannelCmdList.get(strFuzzyWordsCmd);
    }

    public String GetQuerySceneJson(String strSceneName) {
        String strSceneJson = "";
        try {
            JSONArray objFuzzyWords = new JSONArray();
            if (null != mapFuzzyWordsCmdList) {
                for (Map.Entry<String, String> entry : mapFuzzyWordsCmdList.entrySet()) {
                    objFuzzyWords.put(entry.getKey());
                }
            }

            if (null != mapSelectChannelCmdList) {
                for (Map.Entry<String, Integer> entry : mapSelectChannelCmdList.entrySet()) {
                    objFuzzyWords.put(entry.getKey());
                }
            }

            strSceneJson = "{" + "\"_scene\": \"" + strSceneName + "\","
                    + "\"_commands\": {"
                    ///commands
                    + "\"fuzzy_command\": [ \"$W(w_fuzzy_command)\" ]"
                    + "},"
                    ///fuzzy_words
                    + "\"_fuzzy_words\": {"
                    ///change_inputsource
                    + "\"w_fuzzy_command\": "
                    + objFuzzyWords.toString()
                    + "}"
                    + "}";

            Log.v(strTAG, "the " + strSceneName + " raw Json:" + strSceneJson);
        } catch (Exception e) {
            Log.v(strTAG, "catch " + strSceneName + " exception");
            strSceneJson = "";
        }

        Log.v(strTAG, "strSceneJson:" + strSceneJson);
        return strSceneJson;
    }
}
