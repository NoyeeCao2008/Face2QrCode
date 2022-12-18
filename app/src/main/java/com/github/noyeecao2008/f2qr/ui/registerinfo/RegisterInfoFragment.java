package com.github.noyeecao2008.f2qr.ui.registerinfo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.noyeecao2008.f2qr.databinding.FragmentRegisterInfoBinding;
import com.github.noyeecao2008.f2qr.ui.avatar.AvatarCaptureLauncher;
import com.github.noyeecao2008.f2qr.util.QRCodeUtil;

public class RegisterInfoFragment extends Fragment {

    private static final String TAG = RegisterInfoFragment.class.getSimpleName();
    private FragmentRegisterInfoBinding binding;

    private final AvatarCaptureLauncher avartCaputrueLauncher =
            AvatarCaptureLauncher.createLauncher(this,
                    faceId -> {
                        Toast.makeText(RegisterInfoFragment.this.getContext(),
                                "faceId:" + faceId, Toast.LENGTH_LONG).show();
                    }
            );

    // Register the launcher and result handler
    private final QRCodeUtil.QRCodeLauncher barcodeLauncher = QRCodeUtil.createLauncher(this,
            result -> {
                RegisterInfoViewModel dashboardViewModel =
                        new ViewModelProvider(this).get(RegisterInfoViewModel.class);

                if (result.getContents() == null) {
                    Toast.makeText(this.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    dashboardViewModel.setQRContent(result.getContents());
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterInfoViewModel dashboardViewModel =
                new ViewModelProvider(this).get(RegisterInfoViewModel.class);

        binding = FragmentRegisterInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), binding.textQr::setText);
        dashboardViewModel.getQRContent().observe(getViewLifecycleOwner(), content -> {
            if (TextUtils.isEmpty(content)) {
                return;
            }

            Bitmap bitmap = QRCodeUtil.stringToBitmap(content);
            if (bitmap == null) {
                return;
            }
            this.binding.ivQrcode.setImageBitmap(bitmap);
        });

        binding.linearLayoutAvatar.setOnClickListener(v -> {
            avartCaputrueLauncher.launch("hello");
        });
        binding.linearLayoutQRCode.setOnClickListener(v -> {
            barcodeLauncher.launch();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}