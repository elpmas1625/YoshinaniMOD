package com.yoshinani.loadyaml;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class LoadYAML {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String KEY1 = "  - name: ";
    private static final String KEY2 = "    grid: ";
    private static final String YAML_PATH = "config/yoshinanimod/shopmod";
    static File directory = new File(YAML_PATH);
    public static Map<String, Map<Integer, String>> MasterYAML = new HashMap<>();

    public static void init() {

        if(!Main()){LOGGER.info("failed LoadYAML."); System.exit(0);};
        debugModeMap();
    }

    private static boolean Main() {
        File[] files = directory.listFiles();
        if(files == null) {LOGGER.info("file object is null."); return FALSE;}
        for(File file : files){
            if(!file.isFile()) {LOGGER.info("{} does not file.", file.getName()); return FALSE;}
            String[] result = splitFileName(file.getName());  // result[0]: ファイル名, result[1]: 拡張子
            if(Objects.equals(result[1], ".yaml")){LOGGER.info("{} does not have a “yaml” extension.", result[1]); return FALSE;}
            MasterYAML.put(result[0], loadYAML(file.getPath()));
        }
        return TRUE;
    }

    private static Map<Integer, String>  loadYAML(String filePath){
        Map<Integer, String> tmpMap = new HashMap<>();
        try (Scanner scanner = new Scanner(new FileInputStream(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("  - name:")) {
                    tmpMap.put(Integer.valueOf(scanner.nextLine().split(KEY2)[1]), line.split(KEY1)[1]);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.info("よくわからんエラー　その1");
            throw new RuntimeException(e);
        }
        return tmpMap;
    }

    private static String[] splitFileName(String fileNameWithExtension) {
        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return new String[]{fileNameWithExtension, ""};
        } else {
            String fileNameWithoutExtension = fileNameWithExtension.substring(0, lastDotIndex);
            String extension = fileNameWithExtension.substring(lastDotIndex + 1);
            return new String[]{fileNameWithoutExtension, extension};
        }
    }

    private static void debugModeMap() {
        LOGGER.info("ModeMap の内容:");
        System.out.println(MasterYAML);
    }
}