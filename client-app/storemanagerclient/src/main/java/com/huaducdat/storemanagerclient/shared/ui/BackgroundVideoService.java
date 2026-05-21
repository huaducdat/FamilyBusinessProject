package com.huaducdat.storemanagerclient.shared.ui;

import javafx.application.Platform;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ThreadLocalRandom;

public class BackgroundVideoService {

    private static final Path CACHE_ROOT =
            Paths.get(
                    System.getProperty("user.home"),
                    ".storemanagerclient",
                    "cache",
                    "background-video"
            );

    private static final HttpClient HTTP_CLIENT =
            HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

    private static final List<VideoSource> SOURCES =
            List.of(
                    new VideoSource(
                            "city",
                            "https://www.pexels.com/download/video/19109604/"
                    ),
                    new VideoSource(
                            "ocean",
                            "https://www.pexels.com/download/video/35008180/"
                    ),
                    new VideoSource(
                            "rain",
                            "https://videos.pexels.com/video-files/9921183/9921183-uhd_2160_3840_30fps.mp4"
                    ),
                    new VideoSource(
                            "forest",
                            "https://www.pexels.com/download/video/36798462/"
                    ),
                    new VideoSource(
                            "night-drive",
                            "https://www.pexels.com/download/video/33650283/"
                    ),
                    new VideoSource(
                            "aerial-city",
                            "https://www.pexels.com/download/video/35091982/"
                    ),
                    new VideoSource(
                            "cafe",
                            "https://www.pexels.com/download/video/30574535/"
                    )
            );

    private MediaPlayer mediaPlayer;

    private final AtomicLong loadGeneration =
            new AtomicLong();

    public void applyBackground(
            MediaView mediaView,
            Region host
    ) {

        Objects.requireNonNull(mediaView, "mediaView");
        Objects.requireNonNull(host, "host");

        long generation =
                loadGeneration.incrementAndGet();

        mediaView.fitWidthProperty().unbind();
        mediaView.fitHeightProperty().unbind();

        mediaView.setVisible(false);
        mediaView.setOpacity(0.98);
        mediaView.setSmooth(true);
        mediaView.setCache(true);
        mediaView.setPreserveRatio(true);
        mediaView.setMouseTransparent(true);
        mediaView.setEffect(
                createAtmosphericEffect()
        );
        mediaView.fitWidthProperty().bind(
                host.widthProperty()
        );
        mediaView.fitHeightProperty().bind(
                host.heightProperty()
        );

        VideoSource source = pickRandomSource();

        log(
                "Selected video source [" + source.category + "]: "
                        + source.sourceUrl
        );

        CompletableFuture
                .supplyAsync(() -> cacheVideo(source))
                .thenAccept(videoFile -> Platform.runLater(() -> {
                    if (generation != loadGeneration.get()) {
                        log(
                                "Skipping stale video load for "
                                        + source.category
                        );
                        return;
                    }
                    playCachedVideo(mediaView, source, videoFile);
                }))
                .exceptionally(ex -> {
                    log(
                            "Video background failed for "
                                    + source.category
                                    + ": "
                                    + ex.getMessage()
                    );
                    ex.printStackTrace();
                    Platform.runLater(() -> mediaView.setVisible(false));
                    return null;
                });
    }

    public void dispose() {

        loadGeneration.incrementAndGet();

        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception ex) {
                log("Failed to stop media player: " + ex.getMessage());
            }

            try {
                mediaPlayer.dispose();
            } catch (Exception ex) {
                log("Failed to dispose media player: " + ex.getMessage());
            }

            mediaPlayer = null;
        }
    }

    private void playCachedVideo(
            MediaView mediaView,
            VideoSource source,
            Path videoFile
    ) {

        try {
            dispose();

            log(
                    "Playing cached video [" + source.category + "]: "
                            + videoFile.toUri()
            );

            Media media =
                    new Media(
                            videoFile.toUri().toString()
                    );

            mediaView.setMediaPlayer(null);

            MediaPlayer player =
                    new MediaPlayer(media);

            mediaPlayer = player;
            mediaView.setMediaPlayer(player);
            mediaView.setVisible(true);

            player.setMute(true);
            player.setAutoPlay(false);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setOnEndOfMedia(() -> {
                player.seek(Duration.ZERO);
                player.play();
            });
            player.setOnReady(() -> {
                log(
                        "Media ready [" + source.category + "]"
                );
                player.seek(Duration.ZERO);
                player.play();
            });
            player.setOnError(() -> {
                Throwable error = player.getError();
                log(
                        "Media error [" + source.category + "]: "
                                + (error == null ? "unknown" : error.getMessage())
                );
                mediaView.setMediaPlayer(null);
                mediaView.setVisible(false);
            });
        } catch (Throwable ex) {
            log(
                    "Video runtime failed [" + source.category + "]: "
                            + ex.getClass().getName()
                            + " -> "
                            + ex.getMessage()
            );
            ex.printStackTrace();
            mediaView.setMediaPlayer(null);
            mediaView.setVisible(false);
            dispose();
        }
    }

    private javafx.scene.effect.Effect createAtmosphericEffect() {

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.12);
        colorAdjust.setContrast(-0.06);
        colorAdjust.setSaturation(-0.28);

        GaussianBlur blur = new GaussianBlur(16);
        blur.setInput(colorAdjust);

        return blur;
    }

    private Path cacheVideo(
            VideoSource source
    ) {

        try {
            Files.createDirectories(CACHE_ROOT);

            Path categoryCacheDir =
                    CACHE_ROOT.resolve(source.category);

            Files.createDirectories(categoryCacheDir);

            String cacheKey =
                    sha256(source.sourceUrl);

            Path cachedFile =
                    categoryCacheDir.resolve(cacheKey + ".mp4");

            if (Files.exists(cachedFile) && Files.size(cachedFile) > 0) {
                log(
                        "Cache hit [" + source.category + "]: "
                                + cachedFile
                );
                return cachedFile;
            }

            log(
                    "Downloading video [" + source.category + "] to "
                            + cachedFile
            );

            HttpRequest request =
                    HttpRequest.newBuilder(
                                    URI.create(source.sourceUrl)
                            )
                            .header(
                                    "User-Agent",
                                    "Mozilla/5.0"
                            )
                            .header(
                                    "Accept",
                                    "video/mp4,video/*;q=0.9,*/*;q=0.8"
                            )
                            .GET()
                            .build();

            HttpResponse<InputStream> response =
                    HTTP_CLIENT.send(
                            request,
                            HttpResponse.BodyHandlers.ofInputStream()
                    );

            if (response.statusCode() >= 400) {
                throw new IOException(
                        "Video download failed with status "
                                + response.statusCode()
                                + " for "
                                + source.sourceUrl
                );
            }

            try (InputStream inputStream = response.body();
                 OutputStream outputStream =
                         Files.newOutputStream(cachedFile)) {
                inputStream.transferTo(outputStream);
            }

            log(
                    "Cached video [" + source.category + "]: "
                            + cachedFile
            );

            return cachedFile;
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(
                    "Unable to cache video source: " + source.sourceUrl,
                    ex
            );
        }
    }

    private VideoSource pickRandomSource() {

        return SOURCES.get(
                ThreadLocalRandom.current().nextInt(SOURCES.size())
        );
    }

    private String sha256(
            String value
    ) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    value.getBytes(StandardCharsets.UTF_8)
            );
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "Unable to create cache key",
                    ex
            );
        }
    }

    private void log(
            String message
    ) {

        System.out.println(
                "[BackgroundVideoService] " + message
        );
    }

    private record VideoSource(
            String category,
            String sourceUrl
    ) {
    }
}
