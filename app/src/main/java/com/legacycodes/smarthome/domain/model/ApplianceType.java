package com.legacycodes.smarthome.domain.model;

/**
 * The kinds of appliances a user can add.
 *
 * Stored in the DB as the enum NAME (e.g. "LIGHT") — not the ordinal —
 * so reordering this enum later will never corrupt existing data.
 *
 * Each type carries a display label and a Material icon resource name.
 * The icon field is a String (not an int) on purpose: this class has no
 * Android dependency, so the UI layer resolves the name to a drawable.
 */
public enum ApplianceType {

    LIGHT       ("Light",        "ic_light"),
    FAN         ("Fan",          "ic_fan"),
    AC          ("Air Conditioner", "ic_ac"),
    TV          ("TV",           "ic_tv"),
    SOCKET      ("Socket",       "ic_socket"),
    HEATER      ("Heater",       "ic_heater"),
    PUMP        ("Water Pump",   "ic_pump"),
    DOOR_LOCK   ("Door Lock",    "ic_lock"),
    SPEAKER     ("Speaker",      "ic_speaker"),
    OTHER       ("Other",        "ic_other");

    private final String displayName;
    private final String iconName;

    ApplianceType(String displayName, String iconName) {
        this.displayName = displayName;
        this.iconName = iconName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconName() {
        return iconName;
    }

    /**
     * Safe lookup by name — used when reading from the DB.
     * Returns OTHER if the stored name doesn't match any enum
     * (e.g. a type was removed in a future version).
     */
    public static ApplianceType fromName(String name) {
        if (name == null) return OTHER;
        try {
            return ApplianceType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}