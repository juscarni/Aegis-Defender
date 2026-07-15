# HOW TO RUN — Aegis Defender

## Prerequisites

- **Java 21** or higher installed on your system
- Check your Java version with :
  ```bash
  java -version
  ```

---

## Run the game

1. Navigate to the `target/` folder of the project :
   ```bash
   cd target/
   ```

2. Launch the JAR :
   ```bash
   java -jar AegisDefender-1.0.jar
   ```

That's it — the game starts immediately.

---

## Player data

On first launch, the game automatically creates a file to store player scores :

| OS      | Location                                      |
|---------|-----------------------------------------------|
| Windows | `C:\Users\<username>\.aegisdefender\Player.csv` |
| Linux   | `/home/<username>/.aegisdefender/Player.csv`   |

This file is updated automatically after each game session with username, score, kills and date.

---

## Tested on

- Windows 11
- WSL (Windows Subsystem for Linux)