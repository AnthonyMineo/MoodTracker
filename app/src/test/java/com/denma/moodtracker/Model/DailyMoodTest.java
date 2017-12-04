package com.denma.moodtracker.Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DailyMoodTest {

    private DailyMood mDailyMood;

    @Before
    public void instanceDailyMood(){
        mDailyMood = new DailyMood(3, ":)", "this is a test");
    }

    @Test
    public void getId_dailyMood() throws Exception {
        int actual = mDailyMood.getId_dailyMood();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void setId_dailyMood() throws Exception {
        mDailyMood.setId_dailyMood(4);
        int actual = mDailyMood.getId_dailyMood();
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Test
    public void getDailyMood() throws Exception {
        String actual = mDailyMood.getDailyMood();
        String expected = ":)";
        assertEquals(expected, actual);
    }

    @Test
    public void setDailyMood() throws Exception {
        mDailyMood.setDailyMood(":D");
        String actual = mDailyMood.getDailyMood();
        String expected = ":D";
        assertEquals(expected, actual);
    }

    @Test
    public void getDailyCommentary() throws Exception {
        String actual = mDailyMood.getDailyCommentary();
        String expected = "this is a test";
        assertEquals(expected, actual);
    }

    @Test
    public void setDailyCommentary() throws Exception {
        mDailyMood.setDailyCommentary("Hello test !");
        String actual = mDailyMood.getDailyCommentary();
        String expected = "Hello test !";
        assertEquals(expected, actual);
    }

}