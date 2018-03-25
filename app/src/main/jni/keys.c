#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_android_movies_MainActivity_getNativeKey1(JNIEnv *env, jobject instance) {

 return (*env)->  NewStringUTF(env, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
}
