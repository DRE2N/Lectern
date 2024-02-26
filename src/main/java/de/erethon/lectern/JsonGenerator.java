package de.erethon.lectern;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonGenerator {

    public static void generateJSON(List<Integer> menus) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("    \"providers\": [\n");
        int unicode = Lectern.UNICODE_START;
        for (Integer id : menus) {
            unicode+=id;
            addProvider(jsonBuilder, "lectern:gui/" + id + ".png", new String[]{Character.toString((char) unicode)});
        }
        jsonBuilder.append("\n");
        jsonBuilder.append("    ]\n");
        jsonBuilder.append("}\n");
        File folder = new File(Bukkit.getPluginsFolder(), "Lectern/resourcepack/assets/lectern/font/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, "default.json");
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(jsonBuilder.toString());
        } catch (IOException e) {
            System.out.println("An error occurred while writing Lectern JSON file: " + e.getMessage());
        }
    }

    // lets keep support for multiple chars, maybe we need that later
    private static void addProvider(StringBuilder jsonBuilder, String file, String[] chars) {
        jsonBuilder.append("        {\n");
        jsonBuilder.append("            \"type\": \"bitmap\",\n");
        jsonBuilder.append("            \"file\": \"").append(file).append("\",\n");
        jsonBuilder.append("            \"ascent\": ").append(30).append(",\n");
        jsonBuilder.append("            \"height\": ").append(250).append(",\n");
        jsonBuilder.append("            \"chars\": [\n");

        // Add each char entry
        for (int i = 0; i < chars.length; i++) {
            jsonBuilder.append("                \"").append(chars[i]).append("\"");
            if (i < chars.length - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }
        jsonBuilder.append("            ]\n");
        jsonBuilder.append("        }");
    }

}
