package com.baidu.mapclient.liteapp.model;

public class BaiDuConfig {

    /*
        {
       "apiKey": "",
       "ttsAppId": "",
       "ttsAppKey": "",
       "ttsSecretKey": "",
       "ttsAuthSn": "",
   }
    * */

    /**
     * 百度地图ApiKey
     */
    private String apiKey;
    private String ttsAppId;
    private String ttsAppKey;
    private String ttsSecretKey;
    private String ttsAuthSn;


    public BaiDuConfig(String apiKey, String ttsAppId, String ttsAppKey, String ttsSecretKey, String ttsAuthSn) {
        this.apiKey = apiKey;
        this.ttsAppId = ttsAppId;
        this.ttsAppKey = ttsAppKey;
        this.ttsSecretKey = ttsSecretKey;
        this.ttsAuthSn = ttsAuthSn;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTtsAppId() {
        return ttsAppId;
    }

    public void setTtsAppId(String ttsAppId) {
        this.ttsAppId = ttsAppId;
    }

    public String getTtsAppKey() {
        return ttsAppKey;
    }

    public void setTtsAppKey(String ttsAppKey) {
        this.ttsAppKey = ttsAppKey;
    }

    public String getTtsSecretKey() {
        return ttsSecretKey;
    }

    public void setTtsSecretKey(String ttsSecretKey) {
        this.ttsSecretKey = ttsSecretKey;
    }

    public String getTtsAuthSn() {
        return ttsAuthSn;
    }

    public void setTtsAuthSn(String ttsAuthSn) {
        this.ttsAuthSn = ttsAuthSn;
    }

    @Override
    public String toString() {
        return "BaiDuConfig{" + "apiKey='" + apiKey + '\'' + ", ttsAppId='" + ttsAppId + '\'' + ", ttsAppKey='" + ttsAppKey + '\'' + ", ttsSecretKey='" + ttsSecretKey + '\'' + ", ttsAuthSn='" + ttsAuthSn + '\'' + '}';
    }
}
