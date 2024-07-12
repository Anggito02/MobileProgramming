package com.example.platenumberrecognition;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.gms.vision.Frame;
import android.util.SparseArray;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private PreviewView previewViewCamera;
    private OverlayView overlayView;

    private PlateDetectionHelper plateDetectionHelper;
    private TextRecognizer textRecognizer;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            String message = isGranted ? "Camera permission granted" : "Camera permission rejected";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (isGranted) {
                startCamera();
            } else {
                // Tindakan yang sesuai jika izin kamera ditolak
            }
        });

        this.requestPermissionLauncher.launch(Manifest.permission.CAMERA);

        this.previewViewCamera = findViewById(R.id.previewViewCamera);
        this.overlayView = findViewById(R.id.overlayView);

        this.textRecognizer = new TextRecognizer.Builder(this.getApplicationContext()).build();

        try {
            this.plateDetectionHelper = new PlateDetectionHelper(this);
            this.cameraExecutor = Executors.newSingleThreadExecutor();
        } catch (Exception e) {
            Toast.makeText(this, "Failed loading model", Toast.LENGTH_SHORT).show();
            // Tindakan yang sesuai jika gagal memuat model
        }

    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {

            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(this.previewViewCamera.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .build();
                imageAnalysis.setAnalyzer(this.cameraExecutor, new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(@NonNull ImageProxy imageProxy) {
                        try {
                            Bitmap bitmap = Bitmap.createBitmap(imageProxy.getWidth(), imageProxy.getHeight(), Bitmap.Config.ARGB_8888);
                            bitmap.copyPixelsFromBuffer(imageProxy.getPlanes()[0].getBuffer());

                            int rotation = imageProxy.getImageInfo().getRotationDegrees();
                            imageProxy.close();

                            Matrix matrix = new Matrix();
                            matrix.postRotate(rotation);

                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            bitmap.recycle();

                            // Perform OCR
                            List<BoundingBox> objects = MainActivity.this.plateDetectionHelper.process(rotatedBitmap);

                            // Add detected text to bounding box label
                            for (BoundingBox object : objects) {
                                int x1 = (int) Math.min(object.getX1() * (float)rotatedBitmap.getWidth(), rotatedBitmap.getWidth());
                                int y1 = (int) Math.min(object.getY1() * (float)rotatedBitmap.getHeight(), rotatedBitmap.getHeight());
                                int x2 = (int) Math.min(object.getX2() * (float)rotatedBitmap.getWidth(), rotatedBitmap.getWidth());
                                int y2 = (int) Math.min(object.getY2() * (float)rotatedBitmap.getHeight(), rotatedBitmap.getHeight());

                                // Memotong bagian dari bitmap menggunakan createBitmap
                                Bitmap croppedBitmap = Bitmap.createBitmap(rotatedBitmap, x1, y1, x2 - x1, y2 - y1);
                                String detectedText = performOCR(croppedBitmap);
                                croppedBitmap.recycle();
//
                                detectedText = detectedText.replace("\n", " ");
                                object.setLabel(detectedText);
                            }

                            rotatedBitmap.recycle();

                            MainActivity.this.overlayView.setBoundingBox(objects);
                        } catch (Exception ignored) {}
                    }
                });

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        MainActivity.this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        imageAnalysis,
                        preview
                );

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to start camera.", Toast.LENGTH_SHORT).show();
                // Tindakan yang sesuai jika gagal memulai kamera
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private String performOCR(Bitmap bitmap) {
        // Membuat frame dari bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        // Mendapatkan hasil OCR dari frame
        StringBuilder stringBuilder = new StringBuilder();
        SparseArray<TextBlock> textBlocks = MainActivity.this.textRecognizer.detect(frame);
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.valueAt(i);
            stringBuilder.append(textBlock.getValue());
            stringBuilder.append("\n");
        }

        // Mengembalikan teks yang terdeteksi
        return stringBuilder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}