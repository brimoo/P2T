package com.p2t.p2t;


import android.support.annotation.Nullable;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.List;


public class AudioHandler implements Runnable {
    private String result;
    private ByteString audio;
    public AudioHandler(ByteString audio) { this.audio = audio;}

    @Override
    public void run()
    {
        result = getText(audio);
    }

    public String getResult()
    {
        return result;
    }

    private List<SpeechRecognitionResult> getRequest(ByteString audioBytes) {
        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16).setSampleRateHertz(16000).setLanguageCode("en-US").build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();
            RecognizeResponse response = speechClient.recognize(config, audio);
            return response.getResultsList();
        } catch (IOException e) {
        }
        return null;
    }

    private String getText(ByteString audio) {
        List<SpeechRecognitionResult> response = getRequest(audio);
        String str = "";
        for (SpeechRecognitionResult result : response) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            str = str + alternative.getTranscript() + " ";//temporary to see response structure
        }

        return str;
    }
}