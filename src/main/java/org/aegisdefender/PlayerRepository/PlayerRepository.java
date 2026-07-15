package org.aegisdefender.PlayerRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private Path file;

    public PlayerRepository() {
        this.file = initializePlayerDataFile();
    }

    private Path initializePlayerDataFile() {
        Path folder = Paths.get(System.getProperty("user.home"), ".aegisdefender");
        Path csv = folder.resolve("players.csv");

        try {
            Files.createDirectories(folder);
            if (Files.notExists(csv)) {
                System.out.println("File created : " + csv); //
                Files.createFile(csv);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize player data file : " + e.getMessage());
        }
        return csv;
    }

    public void savePlayerData(List<String[]> playerData) {
        try(BufferedWriter writer = Files.newBufferedWriter(file)){
            for (String[] player: playerData) {
                writer.write(player[0] + ";" + player[1] + ";" + player[2] + ";" + player[3]);
                writer.newLine();
            }
            System.out.println("Saved player data : "+ this.file);
        }catch (IOException e){
            System.err.println("Failed to save player data: " + e.getMessage());
        }
    }

    public List<String[]> readPlayerDataFromCsv(){
        List<String[]> PlayerData = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(this.file)){
            String line;
            while((line = reader.readLine()) != null){
                if(line.contains(";")){
                    String[] playerData = line.split(";");
                    PlayerData.add(playerData);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return PlayerData;
    }
}
