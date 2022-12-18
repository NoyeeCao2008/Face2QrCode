package com.github.noyeecao2008.f2qr.ui.avatar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsets;
import android.util.Log;

import com.github.noyeecao2008.f2qr.databinding.ActivityAvatarCaputureBinding;
import com.github.noyeecao2008.net.BuildConfig;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class AvatarCaptureActivity extends AppCompatActivity {
    private static final String TAG = AvatarCaptureActivity.class.getSimpleName();
    private View mContentView;
    private View mControlsView;
    private ActivityAvatarCaputureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAvatarCaputureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;
        String msg = getIntent().getStringExtra("message");
        if (!TextUtils.isEmpty(msg)) {
            binding.fullscreenContent.setText(msg);
        }
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvatarCaptureActivity.this.finishAndSendResult("456");
                Log.i(TAG, "Build.ak = " + BuildConfig.BaiduAiAK);
            }
        });

        if (!XXPermissions.isGranted(this, Permission.CAMERA)) {
            XXPermissions.with(AvatarCaptureActivity.this).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {

                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    finishAndCanceled("No permission for camera");
                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void finishAndSendResult(String faceId) {
        Intent result = new Intent();
        result.putExtra("faceId", faceId);
        setResult(RESULT_OK, result);
        finish();
    }

    private void finishAndCanceled(String message) {
        Intent result = new Intent();
        result.putExtra("message", message);
        setResult(RESULT_CANCELED, result);
        finish();
    }
}