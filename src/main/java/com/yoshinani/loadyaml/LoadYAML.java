package com.yoshinani.loadyaml;

import com.mojang.logging.LogUtils;
import com.yoshinani.shopButton.*;
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

    private static final String TYPE = "  - type: ";
    private static final String YAML_PATH = "config/yoshinanimod/shopmod";
    static File directory = new File(YAML_PATH);
    public static Map<String, Map<Integer, ShopButton>> MasterYAML = new HashMap<>();

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

    private static Map<Integer, ShopButton>  loadYAML(String filePath){
        Map<Integer, ShopButton> tmpMap = new HashMap<>();

        try (Scanner scanner = new Scanner(new FileInputStream(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("  - type:")) {
                    String type = line.split("type: ")[1];
                    String itemId = scanner.nextLine().split("itemId: ")[1];
                    int slotId = Integer.parseInt(scanner.nextLine().split("slotId: ")[1]);
                    String displayName = scanner.nextLine().split("displayName: ")[1];

                    ShopButton shopbutton;
                    switch(type){
                        case "transition":
                            String next = scanner.nextLine().split("next: ")[1];
                            shopbutton = new TransitionButton(itemId, slotId, displayName, next);
                            break;
                        case "amount":
                            String sign = scanner.nextLine().split("sign: ")[1];
                            int amount = Integer.parseInt(scanner.nextLine().split("amount: ")[1]);
                            shopbutton = new AmountButton(itemId, slotId, displayName, sign, amount);
                            break;
                        case "buy":
                            shopbutton = new BuyButton(itemId, slotId, displayName);
                            break;
                        case "plain":
                            shopbutton = new PlainButton(itemId, slotId, displayName);
                            break;
                        case "select":
                            shopbutton = new SelectButton(itemId, slotId, displayName);
                            break;
                        case "sell":
                            shopbutton = new SellButton(itemId, slotId, displayName);
                            break;
                        default:
                            shopbutton = new PlainButton(itemId, slotId, displayName);
                    }
                    tmpMap.put(slotId, shopbutton);
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