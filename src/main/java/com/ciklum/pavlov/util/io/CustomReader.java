package com.ciklum.pavlov.util.io;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CustomReader {
    private final BufferedReader reader;
    private final Charset charset;

    public CustomReader(@NonNull InputStream inputStream, Charset charset) {
        this.charset = charset;
        reader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException exception) {
            throw new IllegalStateException();
        }
    }

    public Charset getCharset() {
        return charset;
    }
}
