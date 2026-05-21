package com.huaducdat.storemanagerclient.shared.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaView;

import java.util.Objects;

public class BackgroundEnvironmentService {

    private final BackgroundImageService backgroundImageService =
            new BackgroundImageService();

    private final BackgroundVideoService backgroundVideoService =
            new BackgroundVideoService();

    private final BackgroundPreferenceService preferenceService =
            new BackgroundPreferenceService();

    private BackgroundMode currentMode;

    public BackgroundMode loadMode() {

        return preferenceService.loadMode();
    }

    public void saveMode(
            BackgroundMode mode
    ) {

        preferenceService.saveMode(mode);
    }

    public void applyMode(
            BackgroundMode mode,
            ImageView imageView,
            MediaView videoView,
            Region host
    ) {

        Objects.requireNonNull(mode, "mode");
        Objects.requireNonNull(imageView, "imageView");
        Objects.requireNonNull(videoView, "videoView");
        Objects.requireNonNull(host, "host");

        if (mode == currentMode) {
            log(
                    "Skipping unchanged mode: " + mode
            );
            return;
        }

        log("Applying mode: " + mode);

        if (mode == BackgroundMode.IMAGE) {
            backgroundVideoService.dispose();
            videoView.setMediaPlayer(null);
            videoView.setVisible(false);
            videoView.setOpacity(0.0);
            videoView.setViewOrder(1.0);
            imageView.setVisible(true);
            imageView.setOpacity(1.0);
            imageView.setViewOrder(0.0);
            backgroundImageService.applyBackground(imageView, host);
        } else {
            backgroundVideoService.dispose();
            videoView.setMediaPlayer(null);
            imageView.setVisible(true);
            imageView.setOpacity(1.0);
            imageView.setViewOrder(1.0);
            backgroundImageService.applyBackground(imageView, host);
            videoView.setVisible(true);
            videoView.setOpacity(0.98);
            videoView.setViewOrder(0.0);
            backgroundVideoService.applyBackground(videoView, host);
        }

        currentMode = mode;
        preferenceService.saveMode(mode);
    }

    public void dispose() {

        backgroundVideoService.dispose();
        currentMode = null;
    }

    private void log(
            String message
    ) {

        System.out.println(
                "[BackgroundEnvironmentService] " + message
        );
    }
}
