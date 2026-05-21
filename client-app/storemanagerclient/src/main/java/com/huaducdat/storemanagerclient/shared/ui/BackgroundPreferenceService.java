package com.huaducdat.storemanagerclient.shared.ui;

import java.util.prefs.Preferences;

public class BackgroundPreferenceService {

    private static final String KEY_BACKGROUND_MODE =
            "background.mode";

    private final Preferences preferences =
            Preferences.userNodeForPackage(
                    BackgroundPreferenceService.class
            );

    public BackgroundMode loadMode() {

        String rawValue =
                preferences.get(
                        KEY_BACKGROUND_MODE,
                        BackgroundMode.VIDEO.name()
                );

        try {
            BackgroundMode mode = BackgroundMode.valueOf(rawValue);
            System.out.println(
                    "[BackgroundPreferenceService] Loaded mode: "
                            + mode
            );
            return mode;
        } catch (IllegalArgumentException ex) {
            System.out.println(
                    "[BackgroundPreferenceService] Invalid stored mode, defaulting to VIDEO"
            );
            return BackgroundMode.VIDEO;
        }
    }

    public void saveMode(
            BackgroundMode mode
    ) {

        if (mode == null) {
            return;
        }

        preferences.put(
                KEY_BACKGROUND_MODE,
                mode.name()
        );

        System.out.println(
                "[BackgroundPreferenceService] Saved mode: "
                        + mode
        );
    }
}
