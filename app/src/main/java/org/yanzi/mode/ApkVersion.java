package org.yanzi.mode;

/**
 * Created by asus on 2017/2/20.
 */

public class ApkVersion {
    private String versionNumber;
    private String importantLevel;
    private String APKUrl;

    public String getAPKUrl() {
        return APKUrl;
    }

    public void setAPKUrl(String APKUrl) {
        this.APKUrl = APKUrl;
    }

    public String getImportantLevel() {
        return importantLevel;
    }

    public void setImportantLevel(String importantLevel) {
        this.importantLevel = importantLevel;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
}
