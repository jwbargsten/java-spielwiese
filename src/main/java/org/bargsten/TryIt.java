package org.bargsten;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static io.vavr.API.$;
import static io.vavr.API.Case;

@Slf4j
public class TryIt {
    HttpClient http = HttpClientBuilder.create().build();
    public static void main(String[] args) {
        blah();
    }


        public void blah3() {
        HttpClient client = HttpClientBuilder.create().build();
        List<String> urls = List.of("http://www.bargsten.org");
        urls.map(HttpGet::new).flatMap(req -> Try.of(() -> client.execute(req)));
        HttpGet httpGet = new HttpGet(urls.get(0));
        Try<HttpResponse> of = Try.of(() -> client.execute(httpGet));
        Try<String> map = of.map(x -> "a");
    }

    public List<Tuple2<String, String>> fetch(final List<String> urls) {
        ObjectMapper mapper = new ObjectMapper();


        Try<String> error = Try.of(() -> {
            throw new RuntimeException("error");
        });

        error.mapFailure(wrap(ex -> ex));


        return urls.map(HttpGet::new)
                .flatMap(req -> Try.of(() -> http.execute(req)))
                .flatMap(res -> Try.of(() -> EntityUtils.toString(res.getEntity(), "UTF-8")))
                .flatMap(entity -> Try.of(() -> mapper.readTree(entity).path("data")))
                .map(json -> Tuple.of(
                        json.get("first_name").toString(),
                        json.get("last_name").toString())
                );



    }

    public static void blah() {


        int nPairs = List.of(1, 2, 2, 1, 1, 3, 5, 1, 2)
                .groupBy(i -> i)
                .map(t -> t._2.length() / 2)
                .sum().intValue();


        log.info("{}", nPairs);

        int[] mixedSocks = {1, 2, 2, 1, 1, 3, 5, 1, 2};    // every number represents a sock color

        // Result = 3  (in order of sock-pair occurrence: 2, 1, 1)
        int numOfPairedSocks = Stream.ofAll(mixedSocks)
                .foldLeft(HashMap.<Integer, Integer>empty(), (acc, num) -> acc.put(num, 1, Integer::sum))
                .foldLeft(0, (acc, entry) -> acc + (entry._2 / 2));
        log.info("{}", numOfPairedSocks);
    }

    public static <T, R> API.Match.Case<T, R> wrap(Function<? super T, ? extends R> f) {
        Objects.requireNonNull(f, "f is null");

        return Case($(), f);
    }
}
