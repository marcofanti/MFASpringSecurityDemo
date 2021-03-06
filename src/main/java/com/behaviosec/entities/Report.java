package com.behaviosec.entities;

import com.behaviosec.config.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

    private boolean isBot = false;
    private boolean isTrained = false;
    private boolean ipChanged = false;
    private boolean isWhitelisted = false;
    private boolean isSessionCorrupted = false;
    private boolean deviceChanged = false;
    private boolean isDataCorrupted = false;
    private boolean isReplay = false;
    private boolean tabAnomaly = false;
    private boolean numpadAnomaly = false;
    private boolean pocAnomaly = false;
    private boolean isRemoteAccess = false;
    private int ipSeverity = 0;
    private long startTimestamp = 0;
    private long endTimestamp = 0;
    private double score = 0.0;
    private double confidence = 0.0;
    private double uiScore = 0.0;
    private double uiConfidence = 0.0;
    private double risk = 0.0;
    private String userid = "";
    private String startDate= "";
    private String endDate = "";
    private String sdkVersion = "";
    private List<Integer> diDesc = null;
    private static final Map<Integer, String> dataIntegrity;
    static {
        dataIntegrity = new HashMap<>();
        dataIntegrity.put(0, "Empty");
        dataIntegrity.put(1, "OK");
        dataIntegrity.put(2, "");
        dataIntegrity.put(3, "Keyboard paste detected.");
        dataIntegrity.put(4, "Paste or autocomplete.");
        dataIntegrity.put(5, "Corrupt integrity meta data.");
        dataIntegrity.put(6, "Mouse paste or autocomplete.");
        dataIntegrity.put(7, "Missing integrity meta data.");
        dataIntegrity.put(8, "Potential paste on an anonymous field is detected (CTRL+V/CMD+V).");
        dataIntegrity.put(9, "Keystrokes in random, non-increasing order.");
        dataIntegrity.put(10, "The timestamps for the data are not increasing as expected (non-incremental).");
        dataIntegrity.put(11, "Paste JavaScript event.");
        dataIntegrity.put(12, "Copy JavaScript event.");
        dataIntegrity.put(13, "Cut JavaScript event.");
        dataIntegrity.put(14, "Anti-fingerprinting mode.");
        dataIntegrity.put(15, "Browser not focused.");
        dataIntegrity.put(16, "Text field not focused.");

    }

    private List<Integer> copyDiDesc = new ArrayList<Integer>(){{
                                                    add(12);
                                                    add(13);
                                                }};

    private List<Integer> pasteDiDesc = new ArrayList<Integer>(){{
        add(3);
        add(6);
        add(11);
    }};

    private List<Integer> deviceDesc;
    private List<Integer> sessionCorruptedUsers;

    public List<Integer> getSessionCorruptedUsers() {
        return sessionCorruptedUsers;
    }

    public void setSessionCorruptedDesc(List<Integer> sessionCorruptedUsers) {
        this.sessionCorruptedUsers = sessionCorruptedUsers;
    }


    public List<Integer> getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(List<Integer> deviceDesc) {
        this.deviceDesc = deviceDesc;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getDataIntegrityReason(int key){
        return dataIntegrity.get(key);
    }
    public boolean hasDataIntegrityFlags(){
        if ( diDesc != null ) {
            return diDesc.size() >0;
        }
        return false;

    }
    public List<Integer> getDiDesc() {
        return diDesc;
    }

    public boolean isCopy(){
        if (! hasDataIntegrityFlags()) {
            return false;
        } else if(Collections.disjoint(copyDiDesc, getDiDesc())){
            return false;
        } else {
            return true;
        }
    }

    public boolean isPaste(){
        if (! hasDataIntegrityFlags()) {
            return false;
        } else if(Collections.disjoint(pasteDiDesc, getDiDesc())){
            return false;
        } else {
            return true;
        }
    }

    public boolean isCopyOrPaste(){
        return isCopy() || isPaste();
    }

    public void setDiDesc(List<Integer> diDesc) {
        this.diDesc = diDesc;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    private String sessionid = "";


    /**
     * data representation constructor
     */
    public Report() { }

    /**
     * auto generated setters and getters
     */

    public boolean isRemoteAccess() {
        return isRemoteAccess;
    }

    public void setRemoteAccess(boolean remoteAccess) {
        isRemoteAccess = remoteAccess;
    }
    public boolean isBot() {
        return isBot;
    }

    public void setIsbot(boolean isbot) {
        this.isBot = isbot;
    }

    public boolean isTrained() {
        return isTrained;
    }

    public void setIsTrained(boolean trained) {
        isTrained = trained;
    }

    public boolean isIpChanged() {
        return ipChanged;
    }

    public void setIpChanged(boolean ipChanged) {
        this.ipChanged = ipChanged;
    }

    public boolean isWhitelisted() {
        return isWhitelisted;
    }

    public void setWhitelisted(boolean whitelisted) {
        isWhitelisted = whitelisted;
    }

    public boolean isSessionCorrupted() {
        return isSessionCorrupted;
    }

    public void setSessionCorrupted(boolean sessionCorrupted) {
        isSessionCorrupted = sessionCorrupted;
    }

    public boolean isDeviceChanged() {
        return deviceChanged;
    }

    public void setDeviceChanged(boolean deviceChanged) {
        this.deviceChanged = deviceChanged;
    }

    public boolean isDataCorrupted() {
        return isDataCorrupted;
    }

    public void setDataCorrupted(boolean dataCorrupted) {
        isDataCorrupted = dataCorrupted;
    }

    public boolean isReplay() {
        return isReplay;
    }

    public void setReplay(boolean replay) {
        isReplay = replay;
    }

    public boolean isTabAnomaly() {
        return tabAnomaly;
    }

    public void setIsTabAnomaly(boolean tabAnomaly) {
        this.tabAnomaly = tabAnomaly;
    }

    public boolean isNumpadAnomaly() {
        return numpadAnomaly;
    }

    public void setIsNumpadAnomaly(boolean numpadAnomaly) {
        this.numpadAnomaly = numpadAnomaly;
    }

    public boolean isPocAnomaly() {
        return pocAnomaly;
    }

    public void setPocAnomaly(boolean pocAnomaly) {
        this.pocAnomaly = pocAnomaly;
    }

    public int getIpSeverity() {
        return ipSeverity;
    }

    public void setIpSeverity(int ipSeverity) {
        this.ipSeverity = ipSeverity;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public double getScore() {
        return score * Constants.SCORE_MULTIPLIER;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getRisk() {
        return this.risk * Constants.SCORE_MULTIPLIER;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public double getConfidence() {
        return confidence * Constants.SCORE_MULTIPLIER;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getUiScore() {
        return uiScore * Constants.SCORE_MULTIPLIER;
    }

    public void setUiScore(double uiScore) {
        this.uiScore = uiScore;
    }

    public double getUiConfidence() {
        return uiConfidence * Constants.SCORE_MULTIPLIER;
    }

    public void setUiConfidence(double uiConfidence) {
        this.uiConfidence = uiConfidence;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString(){
        return  "\nUser : " + getUserid() + "\n"+
                "SessionID : " + getSessionid() + "\n"+
                "Score : " + getScore() + "\n"+
                "Confidence : " + getConfidence() + "\n"+
                "Risk : " + getRisk() + "\n"+
                "Trained : " + isTrained() + "\n"+
                "Bot : " + isBot() + "\n"+
                "";
    }
}