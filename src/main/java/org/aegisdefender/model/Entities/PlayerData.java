package org.aegisdefender.model.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;


public class PlayerData {

    private Map<String, PlayerInfos> data;
    private List<String[]> playerData;

    public PlayerData() {
        this.data = new HashMap<>();
        this.playerData = new ArrayList<>();
    }

    public void loadPlayerData(List<String[]> playerData){
        this.playerData = playerData.isEmpty() ? null : playerData;

        if(this.playerData != null){
            System.out.println("PlayerData have been loaded on memory ");
            for(String[] player : this.playerData){
                if(player.length < 4 ) continue;
                data.put(player[0],
                        new PlayerInfos(player[0],
                                Integer.parseInt(player[1]),
                                Integer.parseInt(player[2]),
                                player[3]
                        ));
            }
        }
    }

    public List<String[]> getData() {
        List<String[]> list = new ArrayList<>();

        data.forEach((key, value) -> {
            list.add(new String[]{value.username,
                            String.valueOf(value.score),
                            String.valueOf(value.kills),
                            value.localDateTime
            }
            );
        });
        // we have to sort the list elements in function of best score
        list.sort(
                Comparator.comparingInt(
                        (String[] row) -> Integer.parseInt(row[1])
                ).reversed()
        );
        return  list;
    }

    public void updateData(String username, int score , int kills, String localDateTime){
        if(data.containsKey(username)){
            if(data.get(username).getScore() < score) {
                data.get(username).setScore(score);
                data.get(username).setKills(kills);
                data.get(username).setLocalDateTime(localDateTime);
            }
        }
        else{
            data.put(username, new PlayerInfos(username, score, kills, localDateTime));
        }
    }

    public int getBestScore(){
        int bestScore = 0;
        if(data.isEmpty()) return bestScore;

        for(Map.Entry<String,PlayerInfos> element : data.entrySet()){
            if(element.getValue().getScore() > bestScore){
                bestScore = element.getValue().getScore();
            }
        }
        return bestScore;
    }

    /********* Inner class  **************/
    public static class PlayerInfos{
        private String username;
        private String localDateTime;
        private int score;
        private int kills;

        public PlayerInfos(String username, int score, int kills , String localDateTime) {
            this.kills = kills;
            this.score = score;
            this.username = username;
            this.localDateTime = localDateTime;
        }

        public int getScore() {
            return score;
        }

        public void setKills(int kills) {
            this.kills = kills;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setLocalDateTime(String localDateTime) {
            this.localDateTime = localDateTime;
        }

        @Override
        public String toString() {
            return username+";"+score+";"+kills+";"+localDateTime; //csv format
        }
    }
}
