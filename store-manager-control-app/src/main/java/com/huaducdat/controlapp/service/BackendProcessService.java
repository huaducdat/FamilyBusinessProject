package com.huaducdat.controlapp.service;

import com.huaducdat.controlapp.util.ConfigUtil;

public class BackendProcessService {

    private Process process;

    // =========================
    // START
    // =========================

    public void start()
            throws Exception {

        if (
                process != null
                        &&
                        process.isAlive()
        ) {

            return;
        }

        String jarPath =

                ConfigUtil.get(
                        "backend.jar.path"
                );

        System.out.println(
                "Backend Path: "
                        + jarPath
        );

        ProcessBuilder builder =
                new ProcessBuilder(

                        "java",

                        "-jar",

                        jarPath
                );

        builder.redirectErrorStream(true);

        builder.inheritIO();

        process =
                builder.start();
    }
    // =========================
    // STOP
    // =========================

    public void stop() {

        if (
                process != null
                        &&
                        process.isAlive()
        ) {

            process.destroy();
        }
    }
}