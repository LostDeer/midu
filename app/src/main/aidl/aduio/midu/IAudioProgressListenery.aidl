// IAudioProgressListenery.aidl
package aduio.midu;

// Declare any non-default types here with import statements

interface IAudioProgressListenery {
     void onProgress(int progress);
     void onDuration(int duration);
}
