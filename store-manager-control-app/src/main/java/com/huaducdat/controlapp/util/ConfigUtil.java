package com.huaducdat.controlapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigUtil {

    private static final Properties
            properties =
            new Properties();

    static {

        try {

            String currentPath =

                    new File(
                            "."
                    ).getAbsolutePath();

            System.out.println(
                    "Current Path: "
                            + currentPath
            );

            File configFile =
                    new File(
                            "config.properties"
                    );

            System.out.println(
                    "Config Exists: "
                            + configFile.exists()
            );

            FileInputStream input =
                    new FileInputStream(
                            configFile
                    );

            properties.load(input);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static String get(
            String key
    ) {

        return properties.getProperty(
                key
        );
    }

    //TECH-PASS
    public static void save(
            String key,
            String value
    ) {

        try {

            properties.setProperty(
                    key,
                    value
            );

            properties.store(
                    new FileOutputStream(
                            "config.properties"
                    ),
                    null
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}