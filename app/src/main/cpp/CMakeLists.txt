cmake_minimum_required(VERSION 3.4.1)

# 配置导入的libs
set(faceVeri_DIR ${CMAKE_SOURCE_DIR}/../../../../faceVeri)

# shared lib will also be tucked into APK and sent to target
# refer to app/build.gradle, jniLibs section for that purpose.
# ${ANDROID_ABI} is handy for our purpose here. Probably this ${ANDROID_ABI} is
# the most valuable thing of this sample, the rest are pretty much normal cmake
# 添加共享库
add_library(lib_face SHARED IMPORTED)
set_target_properties(lib_face PROPERTIES IMPORTED_LOCATION
    ${faceVeri_DIR}/lib/armeabi-v7a/libface.so)

add_library(lib_caffe SHARED IMPORTED)
set_target_properties(lib_caffe PROPERTIES IMPORTED_LOCATION
   ${faceVeri_DIR}/lib/armeabi-v7a/libcaffe.so)


# 编译应用的共享库
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 添加库（JNI文件）
add_library(face-libs SHARED
            face-libs.cpp)

# 指定头文件所在目录（供JNI文件导入）
target_include_directories(face-libs PRIVATE
                           ${faceVeri_DIR}/include)

# 指定链接库
target_link_libraries(face-libs
                      android
                      lib_face
                      lib_caffe
                      log)
