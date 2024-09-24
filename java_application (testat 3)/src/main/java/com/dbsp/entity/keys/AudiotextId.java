package com.dbsp.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class AudiotextId implements Serializable {

    private String asin;
    private String type;
    private String language;
    private String audioformat;

    // Default constructor
    public AudiotextId() {
    }

    // Parameterized constructor
    public AudiotextId(String asin, String type, String language, String audioformat) {
        this.asin = asin;
        this.type = type;
        this.language = language;
        this.audioformat = audioformat;
    }

    // Getters and setters
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAudioformat() {
        return audioformat;
    }

    public void setAudioformat(String audioformat) {
        this.audioformat = audioformat;
    }

    // Override equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AudiotextId that = (AudiotextId) o;
        return Objects.equals(asin, that.asin) &&
                Objects.equals(type, that.type) &&
                Objects.equals(language, that.language) &&
                Objects.equals(audioformat, that.audioformat);
    }

    // Override hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(asin, type, language, audioformat);
    }
}
