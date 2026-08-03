# Simple Ear Trainer

A lightweight Java Swing application designed for ear training. App allows users to switch between piano and guitar, choose same octave mode and change the timing of the notes.

## How to Run

### Option 1: Run via IDE (Eclipse, IntelliJ IDEA, VS Code)

1. **Clone or Download** this repository.
2. Open the project folder in your IDE.
3. Ensure the `src/` directory is set as your **Source Root**.
4. Confirm that the `Assets/` folder is at the **project root level** (outside `src/`).
5. Open `src/driver/Main.java` and click **Run** / **Play**.

### Option 2: Run via Command Line / Terminal

Open your terminal, navigate to the root directory of the project, and run the commands specific to your operating system:

#### **Windows (Command Prompt / `cmd.exe`)**
```cmd
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt
java -cp bin driver.Main

```

#### **Windows (PowerShell)**
```cmd
mkdir bin -Force
javac -d bin (Get-ChildItem -Recurse src/*.java)
java -cp bin driver.Main
```

#### **macOS / Linux**
```cmd
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin driver.Main
```
