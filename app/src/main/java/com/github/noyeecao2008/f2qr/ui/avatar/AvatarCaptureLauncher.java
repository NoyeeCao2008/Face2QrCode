package com.github.noyeecao2008.f2qr.ui.avatar;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.client.android.Intents;

public class AvatarCaptureLauncher {

    public static class AvartContract extends ActivityResultContract<String, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String message) {
            Intent intentScan = new Intent(context, AvatarCaptureActivity.class);
            intentScan.setAction(Intents.Scan.ACTION);
            intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentScan.putExtra("message", message);
            return intentScan;

        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            if (intent == null) {
                return "";
            }
            return intent.getStringExtra("faceId");
        }
    }

    private final ActivityResultLauncher<String> avatarLauncher;

    private AvatarCaptureLauncher(ActivityResultLauncher<String> avatarLauncher) {
        this.avatarLauncher = avatarLauncher;
    }

    public void launch(String msg) {
        this.avatarLauncher.launch(msg);
    }

    public static AvatarCaptureLauncher createLauncher(
            ActivityResultCaller activityResultCaller,
            ActivityResultCallback<String> callback) {
        return new AvatarCaptureLauncher(activityResultCaller.registerForActivityResult(new AvartContract(), callback));
    }
}
