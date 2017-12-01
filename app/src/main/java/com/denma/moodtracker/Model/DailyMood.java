package com.denma.moodtracker.Model;

// Daily Mood object
public class DailyMood {
    private int id_dailyMood;
    private String dailyMoodStat;
    private String dailyCommentary;

    public DailyMood(int id, String mood, String commentary){
        this.id_dailyMood = id;
        this.dailyMoodStat = mood;
        this.dailyCommentary = commentary;
    }

    public int getId_dailyMood() {
        return id_dailyMood;
    }

    public void setId_dailyMood(int id_dailyMood) {
        this.id_dailyMood = id_dailyMood;
    }

    public String getDailyMood() {
        return dailyMoodStat;
    }

    public void setDailyMood(String dailyMood) {
        this.dailyMoodStat = dailyMood;
    }

    public String getDailyCommentary() {
        return dailyCommentary;
    }

    public void setDailyCommentary(String dailyCommentary) {
        this.dailyCommentary = dailyCommentary;
    }
}
