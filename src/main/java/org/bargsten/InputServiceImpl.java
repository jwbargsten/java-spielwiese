package org.bargsten;

import com.google.inject.Inject;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputServiceImpl {
    final FileService fileService;

    @Inject
    public InputServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    public void run() {
        List<String> files = fileService.list();
        files.forEach(f -> log.info("got file {}", f));
    }
}
