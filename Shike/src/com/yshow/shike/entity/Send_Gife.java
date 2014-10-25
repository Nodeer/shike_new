package com.yshow.shike.entity;

import java.io.Serializable;

public class Send_Gife implements Serializable {
    String id;
    String points;
    String name;
    String fileId;
    String faceuri;

    @Override
    public String toString() {
        return "Send_Gife_Gif [id=" + id + ", points=" + points + ", name="
                + name + ", fileId=" + fileId + ", faceuri=" + faceuri + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFaceuri() {
        return faceuri;
    }

    public void setFaceuri(String faceuri) {
        this.faceuri = faceuri;
    }
}
