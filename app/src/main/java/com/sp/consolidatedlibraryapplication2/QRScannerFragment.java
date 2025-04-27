package com.sp.consolidatedlibraryapplication2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.InputStream;

public class QRScannerFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private QRScanListener listener;

    // Define QR Scan Listener
    public interface QRScanListener {
        void onQRScanned(String scannedData);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QRScanListener) {
            listener = (QRScanListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement QRScanListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_scanner, container, false);

        // Pick Image Button Instead of Live Scan
        Button pickImageButton = view.findViewById(R.id.pick_image_button);
        pickImageButton.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void openImagePicker() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                decodeQRCodeFromImage(imageUri);
            }
        }
    }

    private void decodeQRCodeFromImage(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            // Convert image to QR code format
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap);

            if (listener != null) {
                listener.onQRScanned(result.getText());
                dismiss();  // Close the QR scanner dialog
            } else {
                Toast.makeText(getContext(), "QR scan failed!", Toast.LENGTH_SHORT).show();
            }

        } catch (NotFoundException e) {
            Toast.makeText(getContext(), "QR Code not found!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


/*
// With External Device

package com.sp.consolidatedlibraryapplication2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.IntentIntegrator;
import com.journeyapps.barcodescanner.IntentResult;

public class QRScannerFragment extends Fragment {

    private QRScanListener listener;

    // Interface for passing scanned QR data
    public interface QRScanListener {
        void onQRScanned(String scannedData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure the parent activity implements QRScanListener
        if (getActivity() instanceof QRScanListener) {
            listener = (QRScanListener) getActivity();
        }

        // Start QR scanner
        startQRScanner();
    }

    private void startQRScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt("Scan a Room QR Code");  // Custom text on scanner
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);  // Beep sound on scan
        integrator.setCaptureActivity(CaptureActivity.class);  // Start full-screen camera scanner
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();  // Extract QR data

                // Pass scanned data to activity
                if (listener != null) {
                    listener.onQRScanned(scannedData);
                }
            }
        }
    }
}
 */