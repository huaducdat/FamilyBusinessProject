package com.huaducdat.storemanagerclient.shared.ui;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import javafx.application.Platform;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class BackgroundImageService {

    private static final List<String> BACKGROUND_URLS =
            List.of(
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1920&q=80",
                    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1920&q=80",
                    "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1920&q=80",
                    "https://images.unsplash.com/photo-1494526585095-c41746248156?auto=format&fit=crop&w=1920&q=80",
                    "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&w=1920&q=80",
                    "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=1920&q=80"
            );

    public void applyBackground(
            ImageView imageView,
            Region host
    ) {

        Objects.requireNonNull(imageView, "imageView");
        Objects.requireNonNull(host, "host");

        imageView.fitWidthProperty().unbind();
        imageView.fitHeightProperty().unbind();

        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setOpacity(1.0);
        imageView.setEffect(
                createAtmosphericEffect()
        );

        imageView.fitWidthProperty().bind(
                host.widthProperty()
        );
        imageView.fitHeightProperty().bind(
                host.heightProperty()
        );

        String backgroundUrl = pickBackgroundUrl();

        imageView.setVisible(false);
        imageView.setImage(null);

        CompletableFuture
                .supplyAsync(() -> loadImage(backgroundUrl))
                .thenAccept(image -> Platform.runLater(() -> {
                    if (image == null) {
                        imageView.setVisible(false);
                        return;
                    }

                    imageView.setImage(image);
                    imageView.setVisible(true);
                    System.out.println(
                            "[BackgroundImageService] Image loaded successfully"
                    );
                    System.out.println(
                            "[BackgroundImageService] ImageView visible=true"
                    );
                }))
                .exceptionally(ex -> {
                    System.out.println(
                            "[BackgroundImageService] Image failed to load: "
                                    + ex.getMessage()
                    );
                    ex.printStackTrace();
                    Platform.runLater(() -> imageView.setVisible(false));
                    return null;
                });

        System.out.println(
                "[BackgroundImageService] Loading background: "
                        + backgroundUrl
        );
    }

    private javafx.scene.effect.Effect createAtmosphericEffect() {

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.10);
        colorAdjust.setContrast(-0.07);
        colorAdjust.setSaturation(-0.24);

        GaussianBlur blur = new GaussianBlur(18);
        blur.setInput(colorAdjust);

        return blur;
    }

    private Image loadImage(
            String backgroundUrl
    ) {

        int preferredIndex =
                BACKGROUND_URLS.indexOf(backgroundUrl);

        if (preferredIndex >= 0) {
            Image preferredImage =
                    new Image(backgroundUrl, false);

            if (!preferredImage.isError()) {
                System.out.println(
                        "[BackgroundImageService] Image source selected: "
                                + backgroundUrl
                );
                return preferredImage;
            }

            System.out.println(
                    "[BackgroundImageService] Preferred image failed: "
                            + backgroundUrl
            );
        }

        for (String candidate : BACKGROUND_URLS) {
            if (candidate.equals(backgroundUrl)) {
                continue;
            }

            Image image = new Image(candidate, false);

            if (!image.isError()) {
                System.out.println(
                        "[BackgroundImageService] Image source selected: "
                                + candidate
                );
                return image;
            }

            System.out.println(
                    "[BackgroundImageService] Candidate image failed: "
                            + candidate
            );
        }

        Image fallback = new Image(backgroundUrl, false);
        return fallback.isError() ? null : fallback;
    }

    private String pickBackgroundUrl() {

        return BACKGROUND_URLS.get(
                ThreadLocalRandom.current().nextInt(BACKGROUND_URLS.size())
        );
    }
}
