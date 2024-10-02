package com.yoshinani.env;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomEnv {
    private static final String FILE_PATH = "config/yoshinanimod/custom.env";
    public static Map<String, String> data = new HashMap<>();;

    public static void init() {
        try (Scanner scanner = new Scanner(new FileInputStream(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] keyValue = line.split("=");
                if (keyValue.length == 2) {
                    data.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
