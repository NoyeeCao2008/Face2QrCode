package com.github.noyeecao2008.f2qr.ui.scan;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.noyeecao2008.camera.CameraActivityLauncher;
import com.github.noyeecao2008.camera.ImageProcessFactory.FaceInfo;
import com.github.noyeecao2008.camera.utils.ImageCodec;
import com.github.noyeecao2008.db.Face2QrDbMan;
import com.github.noyeecao2008.f2qr.BuildConfig;
import com.github.noyeecao2008.f2qr.R;
import com.github.noyeecao2008.f2qr.databinding.FragmentScanBinding;
import com.github.noyeecao2008.f2qr.util.QRCodeUtil;
import com.github.noyeecao2008.util.ThreadPool;

public class ScanFragment extends Fragment {

    private static final String TAG = ScanFragment.class.getSimpleName();
    private FragmentScanBinding binding;

    // FaceInfo launcher
    private final CameraActivityLauncher faceInfoCaptureLauncher = CameraActivityLauncher.createLauncher(this,
            faceInfo -> {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "faceInfoCaptureLauncher.faceInfo=" + faceInfo);
                }
                String msg = faceInfo.getMsg();
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(ScanFragment.this.getContext(), msg, Toast.LENGTH_LONG).show();
                }

                ScanViewModel model = new ViewModelProvider(this).get(ScanViewModel.class);
                model.setFaceInfo(faceInfo);
                if (!TextUtils.isEmpty(faceInfo.getUserId())) {
                    updateQrcodeByUserId(faceInfo.getUserId());
                }
            }
    );

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScanViewModel scanViewModel =
                new ViewModelProvider(this).get(ScanViewModel.class);

        binding = FragmentScanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonStart.setOnClickListener(v -> {
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                Log.i(TAG, "button[" + tv.getText() + "] clicked");
            } else {
                Log.i(TAG, "button(" + v + ") clicked");
            }

            faceInfoCaptureLauncher.launchForSearch(ScanFragment.this.getContext());
        });
        scanViewModel.getFaceInfo().observe(getViewLifecycleOwner(), this::updateIdentifyFaceInfo);
        scanViewModel.getQrcode().observe(getViewLifecycleOwner(), this::updateQrcode);
        scanViewModel.getRegistrationAvatar().observe(getViewLifecycleOwner(), this::updateRegistrationAvatar);
        return root;
    }

    private void updateRegistrationAvatar(String avatar) {
        if (TextUtils.isEmpty(avatar)) {
            binding.ivAvatarRegistration.setImageResource(R.drawable.ic_baseline_face_100);
            return;
        }
        binding.ivAvatarRegistration.setImageBitmap(
                ImageCodec.INSTANCE.base64ToBitmap(avatar)
        );
    }

    private void updateQrcode(String qrcode) {
        if (TextUtils.isEmpty(qrcode)) {
            binding.ivQrcode.setImageResource(R.drawable.ic_baseline_qr_code_2_200);
            return;
        }
        binding.ivQrcode.setImageBitmap(QRCodeUtil.stringToBitmap(qrcode));
    }

    public void updateIdentifyFaceInfo(FaceInfo faceInfo) {
        if (faceInfo == null || TextUtils.isEmpty(faceInfo.getFaceImgB64())) {
            binding.ivAvatarIdentify.setImageResource(R.drawable.ic_baseline_face_100);
            return;
        }
        binding.ivAvatarIdentify.setImageBitmap(
                ImageCodec.INSTANCE.base64ToBitmap(faceInfo.getFaceImgB64())
        );

    }

    private void updateQrcodeByUserId(String userId) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "updateQrcodeByUserId(" + userId + ")");
        }
        Context context = getContext().getApplicationContext();
        final ScanViewModel scanViewModel =
                new ViewModelProvider(this).get(ScanViewModel.class);

        Face2QrDbMan dbMan = new Face2QrDbMan();
        dbMan.openDb(context);

//        userId = "01537e3c9e71e490ba1f4c14d7b7654b";//TODO TEST
        dbMan.findByUserId(userId, (entity, success) -> {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "updateQrcodeByFaceId = " + entity);
            }
            if (!success || entity == null) {
                ThreadPool.getInstance().postToMainThread(() -> {
                    Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT).show();
                });
                return;
            }
            ThreadPool.getInstance().postToMainThread(() -> {
                scanViewModel.setQrcode(entity.qrInfo);
                scanViewModel.setRegistrationAvatar(entity.avatar);
                dbMan.closeDb();
            });
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}