package org.yanzi.util;

import java.security.PublicKey;

/**
 * Created by asus on 2017/1/10.
 */

public class JniTool {

    public static native String faceDetectCamera(byte[] arr, int w, int h);

    //public static native void testRGB(int[] rgb, int width, int height);

    public static native void init();

    public static native void doIt(byte[] bytes);

    public static native String test();

    public static native String getFaceRect(String imgPath);

    public static native void free();

    public static native int faceFeatureExtractCamera(String imgPath);

    //public static native String faceFeatureExtractCamera1(String imgPath);

    public static native String faceFeatureExtractCamera1(byte[] bytes);

    public static native int faceFeatureExtractIDCard(String imgPath);

    public static native float faceFeatureCompare();

    public static native int faceVeri2(String imgPath1, String imgPath2);

}
