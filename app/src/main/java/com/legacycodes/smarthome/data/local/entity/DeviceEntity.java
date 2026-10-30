package com.legacycodes.smarthome.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Room entity for a single ESP32 / home.
 *
 * Even though the current app supports one device, we store devices in
 * their own table so a future version can add a second home (garage,
 * guest room) without migrating every appliance row.
 *
 * The IP is stored as a String exactly as the user typed it
 * (e.g. "192.168.137.234"). We never store a full URL — the repository
 * builds "http://<ip>" per request so the IP is truly dynamic.
 */
@Entity(
        tableName = "devices",
        indices = { @Index(value = "ip_address", unique = true) }
)
public class DeviceEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "ip_address")
    public String ipAddress;

    /** Epoch millis of last successful /ping, 0 if never. */
    @ColumnInfo(name = "last_seen")
    public long lastSeen;

    /** Populated from /info so the UI can show it before the first real refresh. */
    @ColumnInfo(name = "device_name")
    public String deviceName;

    @ColumnInfo(name = "firmware")
    public String firmware;

    public DeviceEntity() {
        // required by Room + mappers
        this.lastSeen = 0L;
    }

    public DeviceEntity(long id,
                        String name,
                        String ipAddress,
                        long lastSeen,
                        String deviceName,
                        String firmware) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.lastSeen = lastSeen;
        this.deviceName = deviceName;
        this.firmware = firmware;
    }
}