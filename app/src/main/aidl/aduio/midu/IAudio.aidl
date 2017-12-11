// IAudio.aidl
package aduio.midu;
import aduio.midu.IAudioProgressListenery;
import aduio.midu.LocalAudioEntity;
// Declare any non-default types here with import statements

interface IAudio {
    void startAudio(String path,IAudioProgressListenery seekBar);
    void progressAudio(IAudioProgressListenery seekBar);
    void pauseAudio();
    void stopAudio();
    void seekTo(int progress);
    int playState();
    LocalAudioEntity currentPlay();
}
