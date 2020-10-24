package org.bargsten;

import lombok.extern.slf4j.Slf4j;

import io.vavr.collection.List;

@Slf4j
public class Ted {
    public static void main(String[] args) {
        Transactions t = new Transactions();
        List<String> data = t.getData();

//         t.getData();
//        data.add("blah");
//        data.add("blah");
//        log.info("see the data {}", t.getData());


    }
}
