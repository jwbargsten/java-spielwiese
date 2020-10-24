package org.bargsten.di.impl;

import com.google.inject.Inject;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.bargsten.di.api.FileService;

@Slf4j
public class InputService {
    final FileService fileService;

    @Inject
    public InputService(FileService fileService) {
        this.fileService = fileService;
    }

    public void run() {
        List<String> files = fileService.list();
        files.forEach(f -> log.info("got file {}", f));
    }
}
