package com.huaducdat.controlapp.service;

import com.huaducdat.controlapp.util.ConfigUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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

        // =========================
        // WAIT BACKEND READY
        // =========================

        waitBackendReady();

        // =========================
        // OPEN CLIENT
        // =========================

        openClient();
    }

    // =========================
    // WAIT BACKEND READY
    // =========================

    private void waitBackendReady()
            throws Exception {

        while (true) {

            try {

                URL url =
                        new URL(
                                "http://localhost:8080/health"
                        );

                HttpURLConnection conn =
                        (HttpURLConnection)
                                url.openConnection();

                conn.setConnectTimeout(1000);

                conn.connect();

                int code =
                        conn.getResponseCode();

                if (code == 200) {

                    System.out.println(
                            "Backend Ready"
                    );

                    break;
                }

            } catch (Exception ex) {

                System.out.println(
                        "Waiting Backend..."
                );

                Thread.sleep(1000);
            }
        }
    }

    // =========================
    // OPEN CLIENT
    // =========================

    private void openClient()
            throws IOException {

        String clientPath =
                ConfigUtil.get(
                        "client.jar.path"
                );

        String os =
                System.getProperty("os.name")
                        .toLowerCase();

        String javafxPath;

        if (os.contains("win")) {

            javafxPath =
                    "./javafx-sdk-win/lib";

        } else {

            javafxPath =
                    "./javafx-sdk-mac/lib";
        }

        System.out.println(
                "Client Path: "
                        + clientPath
        );

        System.out.println(
                "JavaFX Path: "
                        + javafxPath
        );

        ProcessBuilder builder =
                new ProcessBuilder(

                        "java",

                        "--module-path",
                        javafxPath,

                        "--add-modules",
                        "javafx.controls,javafx.fxml",

                        "-jar",

                        clientPath
                );

        builder.redirectErrorStream(true);

        builder.inheritIO();

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