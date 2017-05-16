/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <cstring>
#include <jni.h>
#include <android/log.h>
#include "FaceVeri.h"
#include <android/log.h>
#include <string>
#include "Util.h"

using namespace std;

#define  LOG_TAG    "Hope"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

const char * intArrToStr(int num1, int num2){

    char str1[10];
    int a = num1;
    sprintf(str1,"%d",a);
    LOGI("---------%s", str1);
    char str2[10];
    int b = num2;
    sprintf(str2,"%d",b);
    LOGI("-------------%s", str2);
    char strLast[20];
    strcpy(strLast, str1);
    strcat(strLast, str2);
    LOGI("----------%s", strLast);
    return strLast;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_yanzi_util_JniTool_faceFeatureExtractCamera1(JNIEnv *env, jclass type, jbyteArray bytes_) {
    jbyte *bytes = env->GetByteArrayElements(bytes_, NULL);

    FACERC  facerc;
    float* f2[1];
    unsigned char* frame = (unsigned char *) bytes;
    int i = faceFeatureExtractCamera(frame,800,600,
                                     facerc,
                                     &f2[0],
                                     1, 0);

    LOGI("这是数值对我来说很重要（逃生）：%i", i);


    char str1[5];
    int a = i;
    sprintf(str1,"%d",a);
    char str2[5];
    int b = facerc.x;
    sprintf(str2,"%d",b);
    char str3[5];
    int c = facerc.y;
    sprintf(str3,"%d",c);
    char str4[5];
    int d = facerc.width;
    sprintf(str4,"%d",d);
    char str5[5];
    int e = facerc.height;
    sprintf(str5,"%d",e);

    char strLast1[25];
    char strLast2[25];
    char strLast3[25];
    char strLast4[25];
    char strLast5[25];
    strcpy(strLast1, str1);
    strcat(strLast1, "/");
    strcpy(strLast2, str2);
    strcat(strLast2, "/");
    strcpy(strLast3, str3);
    strcat(strLast3, "/");
    strcpy(strLast4, str4);
    strcat(strLast4, "/");
    strcpy(strLast5, str5);

    strcat(strLast1, strLast2);
    strcat(strLast1, strLast3);
    strcat(strLast1, strLast4);
    strcat(strLast1, strLast5);

    env->ReleaseByteArrayElements(bytes_, bytes, 0);

    return env->NewStringUTF(strLast1);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_yanzi_util_JniTool_faceDetectCamera(JNIEnv *env, jclass type, jbyteArray arr_, jint w,
                                             jint h) {
    jbyte *arr = env->GetByteArrayElements(arr_, NULL);
    unsigned char* frame = (unsigned char *) arr;
    FACERC  facerc;
    int i = faceDetectCamera(frame, facerc, w, h);

    char str1[5];
    int a = i;
    sprintf(str1,"%d",a);
    char str2[5];
    int b = facerc.x;
    sprintf(str2,"%d",b);
    char str3[5];
    int c = facerc.y;
    sprintf(str3,"%d",c);
    char str4[5];
    int d = facerc.width;
    sprintf(str4,"%d",d);
    char str5[5];
    int e = facerc.height;
    sprintf(str5,"%d",e);

    char strLast1[25];
    char strLast2[25];
    char strLast3[25];
    char strLast4[25];
    char strLast5[25];
    strcpy(strLast1, str1);
    strcat(strLast1, "/");
    strcpy(strLast2, str2);
    strcat(strLast2, "/");
    strcpy(strLast3, str3);
    strcat(strLast3, "/");
    strcpy(strLast4, str4);
    strcat(strLast4, "/");
    strcpy(strLast5, str5);

    strcat(strLast1, strLast2);
    strcat(strLast1, strLast3);
    strcat(strLast1, strLast4);
    strcat(strLast1, strLast5);

    env->ReleaseByteArrayElements(arr_, arr, 0);

    return env->NewStringUTF(strLast1);
}

extern "C"
JNIEXPORT void JNICALL
Java_org_yanzi_util_JniTool_doIt(JNIEnv *env, jclass type, jbyteArray bytes_) {
    jbyte *bytes = env->GetByteArrayElements(bytes_, NULL);

    unsigned char* frame = (unsigned char *) bytes;
    debugTest(frame, 800, 600, "/data/data/org.yanzi.playcamera/camera_img/huni.jpg");
    env->ReleaseByteArrayElements(bytes_, bytes, 0);
}

//extern "C"
//JNIEXPORT jstring JNICALL
//Java_org_yanzi_util_JniTool_faceDetectCamera(JNIEnv *env, jclass type, jintArray arr_, jint w,
//                                             jint h) {
//    jint *arr = env->GetIntArrayElements(arr_, NULL);
//
//    FACERC  facerc;
//    int i = faceDetectCamera(arr, facerc, w, h);
//
//    char str1[5];
//    int a = i;
//    sprintf(str1,"%d",a);
//    char str2[5];
//    int b = facerc.x;
//    sprintf(str2,"%d",b);
//    char str3[5];
//    int c = facerc.y;
//    sprintf(str3,"%d",c);
//    char str4[5];
//    int d = facerc.width;
//    sprintf(str4,"%d",d);
//    char str5[5];
//    int e = facerc.height;
//    sprintf(str5,"%d",e);
//
//    char strLast1[25];
//    char strLast2[25];
//    char strLast3[25];
//    char strLast4[25];
//    char strLast5[25];
//    strcpy(strLast1, str1);
//    strcat(strLast1, "/");
//    strcpy(strLast2, str2);
//    strcat(strLast2, "/");
//    strcpy(strLast3, str3);
//    strcat(strLast3, "/");
//    strcpy(strLast4, str4);
//    strcat(strLast4, "/");
//    strcpy(strLast5, str5);
//
//    strcat(strLast1, strLast2);
//    strcat(strLast1, strLast3);
//    strcat(strLast1, strLast4);
//    strcat(strLast1, strLast5);
//
//    env->ReleaseIntArrayElements(arr_, arr, 0);
//
//    return env->NewStringUTF(strLast1);
//}

//extern "C"
//JNIEXPORT void JNICALL
//Java_org_yanzi_util_JniTool_testRGB(JNIEnv *env, jclass type, jintArray rgb_, jint width,
//                                    jint height) {
//    jint *rgb = env->GetIntArrayElements(rgb_, NULL);
//
//    int* pFrame = rgb;
//    debugTest(pFrame, width, height, "/data/data/org.yanzi.playcamera/camera_img/face.jpg");
//
//
//    env->ReleaseIntArrayElements(rgb_, rgb, 0);
//}



extern "C"
JNIEXPORT void JNICALL
Java_org_yanzi_util_JniTool_free(JNIEnv *env, jclass type) {

    faceVeriFree();

}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_yanzi_util_JniTool_getFaceRect(JNIEnv *env, jclass type, jstring imgPath_) {
    const char *imgPath = env->GetStringUTFChars(imgPath_, 0);
    const char *imgPathA;
    char buf1[100];
    strcpy(buf1,"/data/data/org.yanzi.playcamera/camera_img/");
    strcat(buf1,imgPath);
    imgPathA = (const char*)buf1;

    char* path1 = new char[100];//足够长
    strcpy(path1,imgPathA);

    FACERC  facerc;
    float* f2[1];
    int i = faceFeatureExtractCamera(path1,
                                     facerc,
                                     &f2[0],
                                     0);
    LOGI("照片一识别返回值i为：%i", i);
    if(i != 0){
        LOGI("x：%i", facerc.x);
        LOGI("y：%i", facerc.y);
        LOGI("width：%i", facerc.width);
        LOGI("height：%i", facerc.height);
    }

    char str1[5];
    int a = i;
    sprintf(str1,"%d",a);
    char str2[5];
    int b = facerc.x;
    sprintf(str2,"%d",b);
    char str3[5];
    int c = facerc.y;
    sprintf(str3,"%d",c);
    char str4[5];
    int d = facerc.width;
    sprintf(str4,"%d",d);
    char str5[5];
    int e = facerc.height;
    sprintf(str5,"%d",e);

    char strLast1[25];
    char strLast2[25];
    char strLast3[25];
    char strLast4[25];
    char strLast5[25];
    strcpy(strLast1, str1);
    strcat(strLast1, "/");
    strcpy(strLast2, str2);
    strcat(strLast2, "/");
    strcpy(strLast3, str3);
    strcat(strLast3, "/");
    strcpy(strLast4, str4);
    strcat(strLast4, "/");
    strcpy(strLast5, str5);

    strcat(strLast1, strLast2);
    strcat(strLast1, strLast3);
    strcat(strLast1, strLast4);
    strcat(strLast1, strLast5);

    env->ReleaseStringUTFChars(imgPath_, imgPath);

    return env->NewStringUTF(strLast1);
}

extern "C"
JNIEXPORT jint JNICALL
Java_org_yanzi_util_JniTool_faceFeatureExtractCamera(JNIEnv *env, jclass type, jstring imgPath_) {
    const char *imgPath = env->GetStringUTFChars(imgPath_, 0);

    const char *imgPathA;
    char buf1[100];
    strcpy(buf1,"/data/data/org.yanzi.playcamera/camera_img/");
    strcat(buf1,imgPath);
    imgPathA = (const char*)buf1;

    char* path1 = new char[100];//足够长
    strcpy(path1,imgPathA);

    FACERC  facerc;
    float* f2[1];
    int i = faceFeatureExtractCamera(path1,
                                     facerc,
                                     &f2[0],
                                     1);
    LOGI("照片一识别返回值i为：%i", i);
//    if(i != 0){
//        LOGI("x：%i", facerc.x);
//        LOGI("y：%i", facerc.y);
//        LOGI("width：%i", facerc.width);
//        LOGI("height：%i", facerc.height);
//    }

//    char str1[5];
//    int a = i;
//    sprintf(str1,"%d",a);
//    char str2[5];
//    int b = facerc.x;
//    sprintf(str2,"%d",b);
//    char str3[5];
//    int c = facerc.y;
//    sprintf(str3,"%d",c);
//    char str4[5];
//    int d = facerc.width;
//    sprintf(str4,"%d",d);
//    char str5[5];
//    int e = facerc.height;
//    sprintf(str5,"%d",e);
//
//    char strLast1[25];
//    char strLast2[25];
//    char strLast3[25];
//    char strLast4[25];
//    char strLast5[25];
//    strcpy(strLast1, str1);
//    strcat(strLast1, "/");
//    strcpy(strLast2, str2);
//    strcat(strLast2, "/");
//    strcpy(strLast3, str3);
//    strcat(strLast3, "/");
//    strcpy(strLast4, str4);
//    strcat(strLast4, "/");
//    strcpy(strLast5, str5);
//
//    strcat(strLast1, strLast2);
//    strcat(strLast1, strLast3);
//    strcat(strLast1, strLast4);
//    strcat(strLast1, strLast5);

    env->ReleaseStringUTFChars(imgPath_, imgPath);

    return i;
}

//extern "C"
//JNIEXPORT jstring JNICALL
//Java_org_yanzi_util_JniTool_faceFeatureExtractCamera1(JNIEnv *env, jclass type, jstring imgPath_) {
//    const char *imgPath = env->GetStringUTFChars(imgPath_, 0);
//
//    const char *imgPathA;
//    char buf1[100];
//    strcpy(buf1,"/data/data/org.yanzi.playcamera/camera_img/");
//    strcat(buf1,imgPath);
//    imgPathA = (const char*)buf1;
//
//    char* path1 = new char[100];//足够长
//    strcpy(path1,imgPathA);
//
//    FACERC  facerc;
//    float* f2[1];
//    int i = faceFeatureExtractCamera(path1,
//                                     facerc,
//                                     &f2[0],
//                                     1);
//    LOGI("照片一识别返回值i为：%i", i);
//    if(i != 0){
//        LOGI("x：%i", facerc.x);
//        LOGI("y：%i", facerc.y);
//        LOGI("width：%i", facerc.width);
//        LOGI("height：%i", facerc.height);
//    }
//
//    char str1[5];
//    int a = i;
//    sprintf(str1,"%d",a);
//    char str2[5];
//    int b = facerc.x;
//    sprintf(str2,"%d",b);
//    char str3[5];
//    int c = facerc.y;
//    sprintf(str3,"%d",c);
//    char str4[5];
//    int d = facerc.width;
//    sprintf(str4,"%d",d);
//    char str5[5];
//    int e = facerc.height;
//    sprintf(str5,"%d",e);
//
//    char strLast1[25];
//    char strLast2[25];
//    char strLast3[25];
//    char strLast4[25];
//    char strLast5[25];
//    strcpy(strLast1, str1);
//    strcat(strLast1, "/");
//    strcpy(strLast2, str2);
//    strcat(strLast2, "/");
//    strcpy(strLast3, str3);
//    strcat(strLast3, "/");
//    strcpy(strLast4, str4);
//    strcat(strLast4, "/");
//    strcpy(strLast5, str5);
//
//    strcat(strLast1, strLast2);
//    strcat(strLast1, strLast3);
//    strcat(strLast1, strLast4);
//    strcat(strLast1, strLast5);
//
//    env->ReleaseStringUTFChars(imgPath_, imgPath);
//
//    return env->NewStringUTF(strLast1);
//}

extern "C"
JNIEXPORT jint JNICALL
Java_org_yanzi_util_JniTool_faceFeatureExtractIDCard(JNIEnv *env, jclass type, jstring imgPath_) {
    const char *imgPath = env->GetStringUTFChars(imgPath_, 0);

    const char *imgPathB;
    char buf2[100];
    strcpy(buf2,"/data/data/org.yanzi.playcamera/camera_img/");
    strcat(buf2,imgPath);
    imgPathB = (const char*)buf2;

    char* path2 = new char[100];//足够长
    strcpy(path2,imgPathB);

    FACERC  facerc;
    float* f2[1];
    int j = faceFeatureExtractIDCard(path2,
                                     facerc,
                                     &f2[0]);
    LOGI("照片二识别返回值j为：%i", j);

    env->ReleaseStringUTFChars(imgPath_, imgPath);

    return j;
}



extern "C"
JNIEXPORT jfloat JNICALL
Java_org_yanzi_util_JniTool_faceFeatureCompare(JNIEnv *env, jclass type) {

    float sc[1];
    int similar = faceFeatureCompare(sc);
    LOGI("sc[0]的值为：%f", sc[0]);
    LOGI("similar的值为：%i", similar);

    return sc[0];

}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_yanzi_util_JniTool_test(JNIEnv *env, jclass type) {

    // TODO
    char str1[5];
    int a = 1;
    sprintf(str1,"%d",a);
    char str2[5];
    int b = 30;
    sprintf(str2,"%d",b);
    char str3[5];
    int c = 40;
    sprintf(str3,"%d",c);
    char str4[5];
    int d = 300;
    sprintf(str4,"%d",d);
    char str5[5];
    int e = 500;
    sprintf(str5,"%d",e);

    char strLast1[25];
    char strLast2[25];
    char strLast3[25];
    char strLast4[25];
    char strLast5[25];
    strcpy(strLast1, str1);
    strcat(strLast1, "/");
    strcpy(strLast2, str2);
    strcat(strLast2, "/");
    strcpy(strLast3, str3);
    strcat(strLast3, "/");
    strcpy(strLast4, str4);
    strcat(strLast4, "/");
    strcpy(strLast5, str5);

    strcat(strLast1, strLast2);
    strcat(strLast1, strLast3);
    strcat(strLast1, strLast4);
    strcat(strLast1, strLast5);

    return env->NewStringUTF(strLast1);
}




extern "C"
JNIEXPORT jint JNICALL
Java_org_yanzi_util_JniTool_init(JNIEnv *env, jclass type) {

    LOGI("正在初始化...");
    int i = faceVeriInit("/data/data/org.yanzi.playcamera/files/faceVeriConfig.yml");
    LOGI("初始化完毕");
    return i;

}

extern "C"
JNIEXPORT jint JNICALL
Java_org_yanzi_util_JniTool_faceVeri2(JNIEnv *env, jclass type, jstring imgPath1_,
                                                  jstring imgPath2_) {
    const char *imgPath1 = env->GetStringUTFChars(imgPath1_, 0);
    const char *imgPath2 = env->GetStringUTFChars(imgPath2_, 0);

    const char *imgPathA;
    char buf1[100];
    strcpy(buf1,"/data/data/org.yanzi.playcamera/camera_img/");
    strcat(buf1,imgPath1);
    imgPathA = (const char*)buf1;

    const char *imgPathB;
    char buf2[100];
    strcpy(buf2,"/data/data/org.yanzi.playcamera/camera_img/");
    strcat(buf2,imgPath2);
    imgPathB = (const char*)buf2;

    char* path1 = new char[100];//足够长
    strcpy(path1,imgPathA);
    char* path2 = new char[100];//足够长
    strcpy(path2,imgPathB);

    FACERC  facerc;
    float* f2[1];
    int i = faceFeatureExtractCamera(path1,
                                     facerc,
                                     &f2[0],
                                     1);
    LOGI("照片一识别返回值i为：%i", i);
    int j = faceFeatureExtractIDCard(path2,
                                     facerc,
                                     &f2[0]);
    LOGI("照片二识别返回值j为：%i", j);
    float sc[1];
    int similar = faceFeatureCompare(sc);
    LOGI("sc[0]的值为：%f", sc[0]);
    LOGI("similar的值为：%i", similar);
    env->ReleaseStringUTFChars(imgPath1_, imgPath1);
    env->ReleaseStringUTFChars(imgPath2_, imgPath2);


    return similar;
}


