package com.huaducdat.controlapp.service;

import com.huaducdat.controlapp.util.ConfigUtil;

import java.io.File;

public class DatabaseRestoreService {

    public void restore(
            File sqlFile
    ) throws Exception {

        // =========================
        // CONFIG
        // =========================

        String mysqlPath =

                ConfigUtil.get(
                        "mysql.path"
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

        // =========================
        // COMMAND
        // =========================

        ProcessBuilder builder =
                new ProcessBuilder(

                        mysqlPath,

                        "-u" + username,

                        dbName
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
        // INPUT FILE
        // =========================

        builder.redirectInput(
                sqlFile
        );

        builder.inheritIO();

        // =========================
        // RUN
        // =========================

        Process process =
                builder.start();

        int exitCode =
                process.waitFor();

        // =========================
        // RESULT
        // =========================

        if (exitCode != 0) {

            throw new RuntimeException(
                    "Restore failed"
            );
        }

        System.out.println(
                "Restore success"
        );
    }
}