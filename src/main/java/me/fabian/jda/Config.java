package me.fabian.jda;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Config {

    private JSONObject configObject;
    private final File configFile = new File("config.json");


    Config() {
        Logger logger = LoggerFactory.getLogger(Config.class.getName());

        if (!configFile.exists()) {
            create();
            logger.info("The config file is created. Fill in all the values. If you don't know how to get a value then check out my wiki");
            System.exit(0);
        }

        JSONObject obj = read(configFile);
        if (obj.has("token") &&
                obj.has("prefix") &&
                obj.has("ownerId")) {
            configObject = obj;
        } else {
            logger.error("Missing keys.");
            System.exit(1);
        }
    }

    private void create() {
        try {
            Files.write(Paths.get(configFile.getPath()),
                    new JSONObject()
                            .put("ownerId", "231459866630291459")
                            .put("prefix", "+")
                            .put("token", "")
                            .toString(4)
                            .getBytes()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject read(File file) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(new String(Files.readAllBytes(Paths.get(file.getPath()))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public String getValue(String key) {
        return configObject == null ? null : configObject.get(key).toString();
    }

    public Set<String> getSet(String unLoggedThreads) {
        if (configObject == null) return new HashSet<>();
        Set<String> set = new HashSet<>();
        configObject.getJSONArray(unLoggedThreads).toList().forEach(o -> set.add(o.toString()));
        return set;
    }
}
