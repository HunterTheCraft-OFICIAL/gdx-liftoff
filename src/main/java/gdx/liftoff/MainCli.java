package gdx.liftoff;
//package com.yourcompany.liftoff;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MainCli {

    public static void main(String[] args) {
        // Parse CLI arguments
        Map<String, String> cliArgs = parseArguments(args);

        // Report unknown arguments
        List<String> knownKeys = Arrays.asList(
                "projectName", "packageName", "mainClassName", "projectPath",
                "androidPath", "libgdxVersion", "javaVersion", "appVersion",
                "gwtPluginVersion", "platforms", "extensions", "thirdPartyLibs",
                "addReadme", "addGuiAssets", "gradleTasks"
        );

        cliArgs.keySet().stream()
                .filter(key -> !knownKeys.contains(key))
                .forEach(key -> System.out.println("Warning: unknown argument --" + key));

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

        // Platforms
        String platformsArg = cliArgs.getOrDefault("platforms", String.join(",", userData.platforms));
        userData.platforms = Arrays.stream(platformsArg.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Extensions
        String extensionsArg = cliArgs.getOrDefault("extensions", String.join(",", userData.extensions));
        userData.extensions = Arrays.stream(extensionsArg.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Third-party libraries
        String thirdPartyArg = cliArgs.getOrDefault("thirdPartyLibs", String.join(",", userData.thirdPartyLibs));
        userData.thirdPartyLibs = Arrays.stream(thirdPartyArg.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Optional flags
        userData.addReadme    = Boolean.parseBoolean(cliArgs.getOrDefault("addReadme", Boolean.toString(userData.addReadme)));
        userData.addGuiAssets = Boolean.parseBoolean(cliArgs.getOrDefault("addGuiAssets", Boolean.toString(userData.addGuiAssets)));

        // Gradle tasks
        String gradleTasksArg = cliArgs.getOrDefault("gradleTasks", String.join(",", userData.gradleTasks));
        userData.gradleTasks = Arrays.stream(gradleTasksArg.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Generate project
        Main.generateProject(userData);

        // Detailed feedback
        System.out.println("\n=== Project Created Successfully ===");
        System.out.println("Project Name: " + userData.projectName);
        System.out.println("Project Path: " + userData.projectPath);
        System.out.println("Platforms: " + String.join(", ", userData.platforms));
        System.out.println("Extensions: " + String.join(", ", userData.extensions));
        System.out.println("Third-party libs: " + String.join(", ", userData.thirdPartyLibs));
        System.out.println("Add README: " + userData.addReadme);
        System.out.println("Add GUI Assets: " + userData.addGuiAssets);
        System.out.println("Gradle Tasks: " + String.join(", ", userData.gradleTasks));
        System.out.println("====================================\n");
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
            } else if (arg.startsWith("--")) {
                System.out.println("Warning: Argument ignored (missing '='): " + arg);
            }
        }
        return map;
    }
}