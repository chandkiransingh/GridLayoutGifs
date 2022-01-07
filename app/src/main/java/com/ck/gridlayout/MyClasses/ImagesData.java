package com.ck.gridlayout.MyClasses;

public class ImagesData {
    private String originalImage, previewImage;

    public ImagesData(){}

    public ImagesData(String originalImage, String previewImage) {
        this.originalImage = originalImage;
        this.previewImage = previewImage;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }
}
