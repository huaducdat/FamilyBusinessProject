package com.huaducdat.controlapp.service;

import com.huaducdat.controlapp.util.ConfigUtil;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseBackupService {

    public void backup()
            throws Exception {

        // =========================
        // CONFIG
        // =========================

        String mysqldumpPath =

                ConfigUtil.get(
                        "mysqldump.path"
                );

        String dbName =

                ConfigUtil.get(
                        "db.name"
                );

        String username =

                ConfigUtil.get(
                        "db.username"
                );

        String password =

                ConfigUtil.get(
                        "db.password"
                );

        String backupFolder =

                ConfigUtil.get(
                        "backup.folder"
                );

        // =========================
        // CREATE FOLDER
        // =========================

        File folder =
                new File(
                        backupFolder
                );

        if (!folder.exists()) {

            folder.mkdirs();
        }

        // =========================
        // FILE NAME
        // =========================

        String timestamp =

                LocalDateTime.now()

                        .format(

                                DateTimeFormatter.ofPattern(
                                        "yyyy_MM_dd_HH_mm_ss"
                                )
                        );

        String backupFile =

                backupFolder
                        +

                        "/"

                        +

                        dbName
                        +

                        "_"

                        +

                        timestamp
                        +

                        ".sql";

        // =========================
        // COMMAND
        // =========================

        ProcessBuilder builder =
                new ProcessBuilder(

                        mysqldumpPath,

                        "-u" + username,

                        dbName,

                        "-r",

                        backupFile
                );

        // =========================
        // PASSWORD
        // =========================

        if (
                password != null
                        &&
                        !password.isBlank()
        ) {

            builder.command().add(
                    2,
                    "-p" + password
            );
        }

        // =========================
        // RUN
        // =========================

        builder.inheritIO();

        Process process =
                builder.start();

        int exitCode =
                process.waitFor();

        // =========================
        // RESULT
        // =========================

        if (exitCode != 0) {

            throw new RuntimeException(
                    "Backup failed"
            );
        }

        System.out.println(
                "Backup success: "
                        + backupFile
        );
    }
}