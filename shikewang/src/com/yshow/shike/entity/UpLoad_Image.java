package com.yshow.shike.entity;

public class UpLoad_Image {
    Upload_Filed data;

    public Upload_Filed getData() {
        return data;
    }

    public void setData(Upload_Filed data) {
        this.data = data;
    }

    public class Upload_Filed {
        String fileId;
        String hash;
        String type;
        String path;
        String size;
        String ext;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        @Override
        public String toString() {
            return "Upload_Filed [fileId=" + fileId + ", hash=" + hash
                    + ", type=" + type + ", path=" + path + ", size=" + size
                    + ", ext=" + ext + "]";
        }
    }
}
