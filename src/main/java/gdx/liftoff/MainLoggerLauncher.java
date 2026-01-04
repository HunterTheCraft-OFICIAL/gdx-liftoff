package gdx.liftoff;

import java.io.*;
import java.util.logging.*;

public class MainLoggerLauncher {

    private static final Logger LOGGER = Logger.getLogger("MainLogger");

    public static void main(String[] args) {
        try {
            // Configura logger para arquivo
            FileHandler fh = new FileHandler("main_execution.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(false);

            // Redireciona System.out e System.err
            PrintStream logOut = new PrintStream(new FileOutputStream("main_stdout.log"));
            System.setOut(logOut);
            System.setErr(logOut);

            LOGGER.info("Starting Main.java execution...");

            // Chama o main original
            Main.main(args);

            LOGGER.info("Main.java execution finished.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}