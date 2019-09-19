package com.sangu.apptongji.main.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2016-11-07.
 */

public class MyqrActivity extends BaseActivity {
    private String biaoshi;
    private String key,touxiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_qrcode_generate);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        touxiang = this.getIntent().getStringExtra("touxiang");
        if ("haoyou".equals(biaoshi)) {
            key = "userInfo:";
        }else {
            key = "fukuan:";
        }
        String value = DemoHelper.getInstance().getCurrentUsernName();
        Bitmap qrcode = generateQRCode(key
                + value);
        ImageView imageView = (ImageView) findViewById(R.id.code_image);
        imageView.setImageBitmap(qrcode);
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            // MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,
                    500, 500);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
