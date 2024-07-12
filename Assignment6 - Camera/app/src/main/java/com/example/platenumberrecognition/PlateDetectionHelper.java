package com.example.platenumberrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.util.Size;

import com.example.platenumberrecognition.ml.BestFloat32;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.CastOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlateDetectionHelper {

    private static int IMAGE_SIZE = 640;
    private static final String MODEL_PATH = "best_float32.tflite";
    private static final int THREAD_TOTAL = 4;

    private static float CONFIDENCE_THRESHOLD = 0.25f;
    private static float IOU_THRESHOLD = 0.55f;

    private Interpreter interpreter;

    private Context context;
    int[] offsetEachIndex = null;

    public PlateDetectionHelper(Context context) throws IOException {
        this.context = context;

        MappedByteBuffer model = FileUtil.loadMappedFile(this.context, MODEL_PATH);
        Interpreter.Options option = new Interpreter.Options();
        option.setNumThreads(THREAD_TOTAL);

        interpreter = new Interpreter(model, option);

        this.offsetEachIndex = new int[18];
        this.offsetEachIndex[0] = 0;
        for (int i = 1; i < this.offsetEachIndex.length; i++) {
            this.offsetEachIndex[i] = this.offsetEachIndex[i-1] + 8400;
        }
    }

    public List<BoundingBox> process(Bitmap bitmap) {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, false);

        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new NormalizeOp(0f, 255f))
                .add(new CastOp(DataType.FLOAT32))
                .build();

        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(scaledBitmap);
        TensorImage processedImage = imageProcessor.process(tensorImage);

        TensorBuffer output = TensorBuffer.createFixedSize(new int[]{1 , 18, 8400}, DataType.FLOAT32);
        interpreter.run(processedImage.getBuffer(), output.getBuffer());

        float[] floatArray = output.getFloatArray();

        ArrayList<BoundingBox> result = new ArrayList<>();
        for (int i = 0; i < 8400; i++) {
            // class id 10 - 12 adalah plat nomor
            float confidence = floatArray[this.offsetEachIndex[10] + i];
            for (int j = 11; j <= 12; j++) {
                if (floatArray[this.offsetEachIndex[4 + j] + i] > confidence) {
                    confidence = floatArray[this.offsetEachIndex[4 + j] + i];
                }
            }

            if (confidence > CONFIDENCE_THRESHOLD) {
                float cx = floatArray[this.offsetEachIndex[0] + i];
                float cy = floatArray[this.offsetEachIndex[1] + i];
                float w = floatArray[this.offsetEachIndex[2] + i];
                float h = floatArray[this.offsetEachIndex[3] + i];

                float x1 = Math.max(cx - (w/2f), 0f);
                float y1 = Math.max(cy - (h/2f), 0f);
                float x2 = Math.min(cx + (w/2f), 1f);
                float y2 = Math.min(cy + (h/2f), 1f);

                result.add(new BoundingBox(x1, y1, x2, y2, confidence));
            }
        }

        scaledBitmap.recycle();

        return this.NMS(result);
    }

    private List<BoundingBox> NMS(List<BoundingBox> boxes) {
        ArrayList<BoundingBox> result = new ArrayList<>();

        for (BoundingBox box : boxes) {
            boolean addNewBox = true;

            for (int i = 0; i < result.size(); i++) {
                BoundingBox otherBox = result.get(i);
                if (this.intersectionOverUnion(box, otherBox) > IOU_THRESHOLD) {
                    if (otherBox.getConfidence() < box.getConfidence()) {
                        result.set(i, box);
                    }
                    addNewBox = false;
                    break;
                }
            }

            if (addNewBox) {
                result.add(box);
            }
        }

        return result;
    }

    private float intersectionOverUnion(BoundingBox box0, BoundingBox box1) {
        float xOverlap = Math.max(Math.min(box0.getX2() - box1.getX1(), box1.getX2() - box0.getX1()), 0f);
        float yOverlap = Math.max(Math.min(box0.getY2() - box1.getY1(), box1.getY2() - box0.getY1()), 0f);

        float areaOverlap = xOverlap * yOverlap;
        float firstBoxArea = (box0.getX2() - box0.getX1()) * (box0.getY2() - box0.getY1());
        float secondBoxArea = (box1.getX2() - box1.getX1()) * (box1.getY2() - box1.getY1());

        return areaOverlap / (firstBoxArea + secondBoxArea - areaOverlap);
    }
}
