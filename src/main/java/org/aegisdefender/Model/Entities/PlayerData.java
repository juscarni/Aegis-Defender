package org.aegisdefender.Model.Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {

    private Map<String, PlayerInfos> data;

    public PlayerData() {
        this.data = new HashMap<>();
    }

    public Map<String, PlayerInfos> getData() {
        return data;
    }

    public void updateData(String username, int score , int kills, LocalDateTime localeDateTime){
        if(data.containsKey(username)){
            if(data.get(username).getScore() < score) {
                data.get(username).setScore(score);
                data.get(username).setKills(kills);
                data.get(username).setLocalDateTime(localeDateTime);
            }
        }
        else{
            data.put(username, new PlayerInfos(username, score, kills, localeDateTime));
        }
    }

    /********* Inner class  **************/
    public static class PlayerInfos{
        private String username;
        private String localDateTime;
        private int score;
        private int kills;

        public PlayerInfos(String username, int score, int kills , LocalDateTime localDateTime) {
            this.kills = kills;
            this.score = score;
            this.username = username;
            this.localDateTime = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy | hh:mm:ss"));
        }

        public String getUsername() {
            return username;
        }

        public String getLocalDateTime() {
            return localDateTime;
        }

        public int getScore() {
            return score;
        }

        public int getKills() {
            return kills;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setKills(int kills) {
            this.kills = kills;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy | hh:mm:ss"));
        }

        @Override
        public String toString() {
            return username+";"+score+";"+kills+";"+localDateTime; //csv format
        }
    }
}
