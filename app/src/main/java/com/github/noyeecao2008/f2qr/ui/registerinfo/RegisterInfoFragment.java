package com.github.noyeecao2008.f2qr.ui.registerinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.noyeecao2008.camera.CameraActivityLauncher;
import com.github.noyeecao2008.camera.ImageProcessFactory;
import com.github.noyeecao2008.camera.utils.ImageCodec;
import com.github.noyeecao2008.db.Face2QrDbMan;
import com.github.noyeecao2008.f2qr.R;
import com.github.noyeecao2008.f2qr.databinding.FragmentRegisterInfoBinding;
import com.github.noyeecao2008.f2qr.util.QRCodeUtil;
import com.github.noyeecao2008.util.ThreadPool;

public class RegisterInfoFragment extends Fragment {

    private static final String TAG = RegisterInfoFragment.class.getSimpleName();
    private FragmentRegisterInfoBinding binding;

    // QRCode launcher
    private final QRCodeUtil.QRCodeLauncher qrcodeLauncher = QRCodeUtil.createLauncher(this,
            qrcode -> {
                RegisterInfoViewModel registerViewModel =
                        new ViewModelProvider(this).get(RegisterInfoViewModel.class);

                if (qrcode.getContents() == null) {
                    Toast.makeText(this.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getContext(), "Scanned: " + qrcode.getContents(), Toast.LENGTH_LONG).show();
                    registerViewModel.setQRContent(qrcode.getContents());

                    // Launcher the faceInfo Capture
                    RegisterInfoFragment.this.faceInfoCaptureLauncher.launchForAdd(
                            RegisterInfoFragment.this.getContext(), registerViewModel.getUserId());
                }
            });

    // QRCode Observer
    private Observer<? super String> mQrcodeObserve = new Observer<String>() {
        @Override
        public void onChanged(String content) {
            if (TextUtils.isEmpty(content)) {
                binding.ivQrcode.setImageResource(R.drawable.ic_baseline_qr_code_2_200);
                return;
            }

            Bitmap bitmap = QRCodeUtil.stringToBitmap(content);
            if (bitmap == null) {
                return;
            }
            binding.ivQrcode.setImageBitmap(bitmap);
        }
    };

    // Avatar Capture Launcher
    private final CameraActivityLauncher faceInfoCaptureLauncher = CameraActivityLauncher.createLauncher(this,
            faceInfo -> {
                RegisterInfoViewModel dashboardViewModel =
                        new ViewModelProvider(this).get(RegisterInfoViewModel.class);
                dashboardViewModel.setFaceInfo(faceInfo);
                if (faceInfo != null && !TextUtils.isEmpty(faceInfo.getMsg())) {
                    Toast.makeText(RegisterInfoFragment.this.getContext(), faceInfo.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
    );

    // FaceInfo Capture Observer
    private Observer<? super ImageProcessFactory.FaceInfo> mFaceInfoObserve = new Observer<ImageProcessFactory.FaceInfo>() {
        @Override
        public void onChanged(ImageProcessFactory.FaceInfo faceInfo) {
            if (faceInfo == null) {
                binding.ivAvatarRegistration.setImageResource(R.drawable.ic_baseline_face_100);
                return;
            }
            if (!TextUtils.isEmpty(faceInfo.getFaceImgB64())) {
                binding.ivAvatarRegistration.setImageBitmap(
                        ImageCodec.INSTANCE.base64ToBitmap(faceInfo.getFaceImgB64())
                );
                RegisterInfoViewModel dashboardViewModel =
                        new ViewModelProvider(RegisterInfoFragment.this).get(RegisterInfoViewModel.class);
                final String qrcode = dashboardViewModel.getQRContent().getValue();
                final Context context = RegisterInfoFragment.this.getContext().getApplicationContext();
                ThreadPool.getInstance().postToBackground(() -> {
                    Face2QrDbMan dbman = new Face2QrDbMan();
                    dbman.openDb(context);
                    dbman.insert(faceInfo.getUserId(), qrcode, faceInfo.getFaceImgB64());
                    dbman.closeDb();
                });

            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterInfoViewModel dashboardViewModel =
                new ViewModelProvider(this).get(RegisterInfoViewModel.class);

        binding = FragmentRegisterInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dashboardViewModel.getFaceInfo().observe(getViewLifecycleOwner(), mFaceInfoObserve);
        dashboardViewModel.getQRContent().observe(getViewLifecycleOwner(), mQrcodeObserve);

        binding.buttonStart.setOnClickListener(v -> {
            qrcodeLauncher.launch();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}