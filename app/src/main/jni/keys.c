#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_android_movies_MainActivity_getNativeKey1(JNIEnv *env, jobject instance) {
 // Your API KEY here "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" - Base64 encoded
 return (*env)->  NewStringUTF(env, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
}
