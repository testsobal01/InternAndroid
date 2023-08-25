package com.example.makotomurase;

public class Constrants {
    public enum START_OPTION {
        NEW, CONTINUE
    }
    public final static String KEY_INTENT_START_OPTION = "start_option";
    public final static String KEY_INTENT_RANGE = "random_range";
    public static final int RANGE_RANDOM_MAX = 50;
    public static final int RANGE_RANDOM_MIN = 10;
    public static final int SCORE_WIN = 20;
    public static final int SCORE_LOSE = -10;
    public static final int SCORE_DRAW = 10;
    public static final int DELAY_NEXT_TEXT_UPDATE = 500;
    public static final int DELAY_AFTER_GAME = 1000;
    public static final int DURATION_ANIM_NUMBER = 700;
}
