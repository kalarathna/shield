package com.kaladevi.shield.service;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter
public class CustomMultipartFile {

    private final byte[] fileContent;

    private String fileName;

    private String contentType;

    private File file;

    private String destPath = System.getProperty("java.io.tmpdir");

    private FileOutputStream fileOutputStream;

    public CustomMultipartFile(byte[] fileData, String name) {
        this.fileContent = fileData;
        this.fileName = name;
        file = new File(destPath + fileName);

    }

    //@Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(fileContent);
    }

    public void clearOutStreams() throws IOException {
        if (null != fileOutputStream) {
            fileOutputStream.flush();
            fileOutputStream.close();
            file.deleteOnExit();
        }
    }

    //@Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    //@Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }
}
