package org.bargsten.di.api;

import io.vavr.collection.List;

import java.io.InputStream;

public interface FileService {
    List<String> list();

    String getContent(String path);

    InputStream getContentStream(String path);
}
