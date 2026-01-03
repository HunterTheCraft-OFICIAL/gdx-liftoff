package gdx.liftoff.cli;

import gdx.liftoff.Listing;
import gdx.liftoff.data.languages.Language;
import gdx.liftoff.data.libraries.Library;
import gdx.liftoff.data.platforms.Platform;
import gdx.liftoff.data.templates.Template;

import java.util.*;

public class LiftoffCLI {

    public static void main(String[] args) {
        Map<String, String> params = parseArgs(args);

        String projectName = params.getOrDefault("project", "MyGame");
        String platformId = params.get("platform");
        String languageId = params.get("language");
        String templateId = params.get("template");
        String libsParam = params.get("libs");

        // Escolher plataforma
        Platform platform = platformId != null ? Listing.platformsByName.get(platformId) : Listing.platforms.get(0);

        // Escolher linguagem
        Language language = languageId != null ? Listing.chooseLanguages(Arrays.asList(languageId)).get(0) : Listing.languages.get(0);

        // Escolher template
        Template template = templateId != null ? Listing.templatesByName.get(templateId) : Listing.templates.get(0);

        // Escolher bibliotecas
        List<Library> chosenLibs = new ArrayList<>();
        if (libsParam != null) {
            String[] libIds = libsParam.split(",");
            chosenLibs.addAll(Listing.chooseOfficialLibraries(Arrays.asList(libIds)));
            chosenLibs.addAll(Listing.chooseUnofficialLibraries(Arrays.asList(libIds)));
        }

        // Mostrar resumo do projeto no console
        System.out.println("=== Liftoff CLI ===");
        System.out.println("Projeto: " + projectName);
        System.out.println("Plataforma: " + (platform != null ? platform.getName() : "default"));
        System.out.println("Linguagem: " + (language != null ? language.getId() : "default"));
        System.out.println("Template: " + (template != null ? template.getId() : "default"));
        System.out.print("Bibliotecas: ");
        if (chosenLibs.isEmpty()) {
            System.out.println("Nenhuma selecionada");
        } else {
            chosenLibs.forEach(lib -> System.out.print(lib.getId() + " "));
            System.out.println();
        }

        // Aqui chamamos o Maker ou outro fluxo do Liftoff que cria o projeto
        System.out.println("\nCriando projeto...");
        // Maker.createProject(projectName, platform, language, template, chosenLibs);
        System.out.println("Projeto criado com sucesso!");
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--project":
                    map.put("project", args[++i]);
                    break;
                case "--platform":
                    map.put("platform", args[++i]);
                    break;
                case "--language":
                    map.put("language", args[++i]);
                    break;
                case "--template":
                    map.put("template", args[++i]);
                    break;
                case "--libs":
                    map.put("libs", args[++i]);
                    break;
                default:
                    System.out.println("Parâmetro desconhecido: " + args[i]);
            }
        }
        return map;
    }
}