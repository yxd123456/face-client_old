package org.yanzi.mode;

import java.io.Serializable;

/**
 * Created by asus on 2017/1/12.
 */

//tv_info.setText("姓名：" + HsOtgService.ic.getPeopleName() + "\n" + "性别：" + HsOtgService.ic.getSex() + "\n" + "民族：" + HsOtgService.ic.getPeople()
//+ "\n" + "出生日期：" + df.format(HsOtgService.ic.getBirthDay()) + "\n" + "地址：" + HsOtgService.ic.getAddr() + "\n" + "身份号码："
// + HsOtgService.ic.getIDCard() + "\n" + "签发机关：" + HsOtgService.ic.getDepartment() + "\n" + "有效期限：" + HsOtgService.ic.getStrartDate()
//+ "-" + HsOtgService.ic.getEndDate() + "\n"+m_FristPFInfo+"\n"+m_SecondPFInfo);

public class FaceData implements Serializable{

    private int id;
    private String cameraFaceImg;//摄像头图片流
    private String idCardFaceImg;//身份证图片流
    private float similar;//人脸相似度
    private String name;//姓名
    private String sex;//性别
    private String people;//民族
    private String dateOfBirth;//出生日期
    private String addr;//住址
    private String code;//身份证号码
    private String department;//签发机关
    private String startDate;//身份证有效期开始时间
    private String endDate;//身份证有效期结束时间
    private String deviceId;//设备ID



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    private String currentTime;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCameraFaceImg() {
        return cameraFaceImg;
    }

    public void setCameraFaceImg(String cameraFaceImg) {

        this.cameraFaceImg = cameraFaceImg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCardFaceImg() {
        return idCardFaceImg;
    }

    public void setIdCardFaceImg(String idCardFaceImg) {

        this.idCardFaceImg = idCardFaceImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
