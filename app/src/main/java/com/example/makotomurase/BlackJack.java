package com.example.makotomurase;

public class BlackJack {
    public int calcScore(int haveScore, int Score) {
        int getScore=0;
        haveScore-=50;

        if(Score>=1){
            getScore=50*Score;
        }
        haveScore += getScore;
        return haveScore;
    }
}
