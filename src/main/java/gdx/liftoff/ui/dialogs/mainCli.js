package com.yourcompany.liftoff;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MainCli {

    public static void main(String[] args) {
        // Map to store CLI arguments
        Map<String, String> cliArgs = parseArguments(args);

        // Populate UserData with CLI values or defaults
        UserData userData = new UserData();

        userData.projectName       = cliArgs.getOrDefault("projectName", userData.projectName);
        userData.packageName       = cliArgs.getOrDefault("packageName", userData.packageName);
        userData.mainClassName     = cliArgs.getOrDefault("mainClassName", userData.mainClassName);
        userData.projectPath       = cliArgs.getOrDefault("projectPath", userData.projectPath);
        userData.androidPath       = cliArgs.getOrDefault("androidPath", userData.androidPath);
        userData.libgdxVersion     = cliArgs.getOrDefault("libgdxVersion", userData.libgdxVersion);
        userData.javaVersion       = cliArgs.getOrDefault("javaVersion", userData.javaVersion);
        userData.appVersion        = cliArgs.getOrDefault("appVersion", userData.appVersion);
        userData.gwtPluginVersion  = cliArgs.getOrDefault("gwtPluginVersion", userData.gwtPluginVersion);

        // Platforms (comma-separated list)
        String platformsArg = cliArgs.getOrDefault("platforms", String.join(",", userData.platforms));
        userData.platforms = Arrays.asList(platformsArg.split(","));

        // Extensions (comma-separated list)
        String extensionsArg = cliArgs.getOrDefault("extensions", String.join(",", userData.extensions));
        userData.extensions = Arrays.asList(extensionsArg.split(","));

        // Third-party libraries
        String thirdPartyArg = cliArgs.getOrDefault("thirdPartyLibs", String.join(",", userData.thirdPartyLibs));
        userData.thirdPartyLibs = Arrays.asList(thirdPartyArg.split(","));

        // Optional flags
        userData.addReadme    = Boolean.parseBoolean(cliArgs.getOrDefault("addReadme", Boolean.toString(userData.addReadme)));
        userData.addGuiAssets = Boolean.parseBoolean(cliArgs.getOrDefault("addGuiAssets", Boolean.toString(userData.addGuiAssets)));

        // Gradle tasks (comma-separated)
        String gradleTasksArg = cliArgs.getOrDefault("gradleTasks", String.join(",", userData.gradleTasks));
        userData.gradleTasks = Arrays.asList(gradleTasksArg.split(","));

        // Generate project using the main generator
        Main.generateProject(userData);

        System.out.println("Project creation completed successfully!");
    }

    /**
     * Parses CLI arguments of the form --key=value
     */
    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                String[] split = arg.substring(2).split("=", 2);
                map.put(split[0], split[1]);
            }
        }
        return map;
    }
}