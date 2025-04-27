package com.sp.consolidatedlibraryapplication2;

import com.google.zxing.LuminanceSource;

public class RGBLuminanceSource extends LuminanceSource {
    private final byte[] luminances;

    public RGBLuminanceSource(int width, int height, int[] pixels) {
        super(width, height);

        int size = width * height;
        luminances = new byte[size];

        for (int i = 0; i < size; i++) {
            int pixel = pixels[i];
            int red = (pixel >> 16) & 0xff; // Extract Red
            int green = (pixel >> 8) & 0xff; // Extract Green
            int blue = pixel & 0xff; // Extract Blue
            luminances[i] = (byte) ((red + green + blue) / 3); // Convert to grayscale
        }
    }

    @Override
    public byte[] getMatrix() {
        return luminances;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (row == null) {
            row = new byte[getWidth()];
        }
        System.arraycopy(luminances, y * getWidth(), row, 0, getWidth());
        return row;
    }
}
