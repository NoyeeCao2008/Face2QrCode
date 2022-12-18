package com.github.noyeecao2008.f2qr.util;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRCodeUtil {
    private static final String TAG = QRCodeUtil.class.getSimpleName();

    public static Bitmap stringToBitmap(String content) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 1024, 1024);
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public static class QRCodeLauncher {
        private final ActivityResultLauncher<ScanOptions> barcodeLauncher;

        public QRCodeLauncher(ActivityResultLauncher<ScanOptions> barcodeLauncher) {
            this.barcodeLauncher = barcodeLauncher;
        }

        public void launch() {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a barcode");
            options.setBeepEnabled(true);
            options.setBarcodeImageEnabled(true);
            this.barcodeLauncher.launch(options);
        }
    }

    public static QRCodeLauncher createLauncher(ActivityResultCaller activityResultCaller, ActivityResultCallback<ScanIntentResult> callback) {
        return new QRCodeLauncher(activityResultCaller.registerForActivityResult(new ScanContract(), callback));
    }
}
