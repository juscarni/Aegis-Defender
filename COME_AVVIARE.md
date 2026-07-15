# COME AVVIARE — Aegis Defender

## Prerequisiti

- **Java 21** o versione superiore installato sul sistema
- Verifica la versione di Java con :
  ```bash
  java -version
  ```

---

## Avviare il gioco

1. Naviga nella cartella `target/` del progetto :
   ```bash
   cd target/
   ```

2. Lancia il JAR :
   ```bash
   java -jar AegisDefender-1.0.jar
   ```

Il gioco si avvia immediatamente.

---

## Dati del giocatore

Al primo avvio, il gioco crea automaticamente un file per salvare i punteggi dei giocatori :

| Sistema Operativo | Percorso                                              |
|-------------------|-------------------------------------------------------|
| Windows           | `C:\Users\<nomeutente>\.aegisdefender\Player.csv`     |
| Linux             | `/home/<nomeutente>/.aegisdefender/Player.csv`        |

Questo file viene aggiornato automaticamente al termine di ogni partita con username, punteggio, kill e data.

---

## Testato su

- Windows 11
- WSL (Windows Subsystem for Linux)