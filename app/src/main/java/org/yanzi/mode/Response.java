package org.yanzi.mode;

/**
 * Created by asus on 2017/2/16.
 */

public class Response {
    private String CameraFaceImgsuccess;
    private String IdCardFaceImgsuccess;
    private String DatabaseSaveMark;

    public String getCameraFaceImgsuccess() {
        return CameraFaceImgsuccess;
    }

    public void setCameraFaceImgsuccess(String cameraFaceImgsuccess) {
        CameraFaceImgsuccess = cameraFaceImgsuccess;
    }

    public String getDatabaseSaveMark() {
        return DatabaseSaveMark;
    }

    public void setDatabaseSaveMark(String databaseSaveMark) {
        DatabaseSaveMark = databaseSaveMark;
    }

    public String getIdCardFaceImgsuccess() {
        return IdCardFaceImgsuccess;
    }

    public void setIdCardFaceImgsuccess(String idCardFaceImgsuccess) {
        IdCardFaceImgsuccess = idCardFaceImgsuccess;
    }
}
