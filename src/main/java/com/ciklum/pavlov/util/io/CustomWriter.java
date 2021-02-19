package com.ciklum.pavlov.util.io;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomWriter {
    private final BufferedWriter writer;
    private final OutputStream innerOutputStream;
    private final Charset charset;

    public CustomWriter(@NonNull OutputStream writer, Charset charset) {
        this.charset = charset;
        innerOutputStream = writer;
        this.writer = new BufferedWriter(new OutputStreamWriter(writer, charset));
    }

    public void print(String line) {
        try {
            writer.write(line);
        } catch (IOException exception) {
            log.error("An io exception has been occurred during print method executing!" + exception.getLocalizedMessage());
            throw new IllegalStateException();
        }
    }

    public void println(String line) {
        try {
            print(line);
            writer.newLine();
            writer.flush();
        } catch (IOException exception) {
            log.error("An io exception has been occurred during println method executing!" + exception.getLocalizedMessage());
            throw new IllegalStateException();
        }
    }

    public OutputStream getInnerOutputStream() {
        return innerOutputStream;
    }

    public Charset getCharset() {
        return this.charset;
    }
}
