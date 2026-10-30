package com.legacycodes.smarthome;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PingClient {

    public interface Callback {
        void onSuccess(String body);
        void onFailure(String reason);
    }

    private final OkHttpClient client;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public PingClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void ping(String ipAddress, Callback callback) {
        String url = "http://" + ipAddress + "/ping";

        executor.execute(() -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    String body = response.body() != null ? response.body().string() : "";
                    if (response.isSuccessful()) {
                        mainHandler.post(() -> callback.onSuccess(body));
                    } else {
                        mainHandler.post(() ->
                                callback.onFailure("HTTP " + response.code()));
                    }
                }
            } catch (IOException e) {
                String reason = e.getMessage() != null ? e.getMessage() : "Network error";
                mainHandler.post(() -> callback.onFailure(reason));
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}