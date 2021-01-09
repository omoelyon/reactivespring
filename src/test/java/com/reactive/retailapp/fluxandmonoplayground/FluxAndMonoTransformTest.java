package com.reactive.retailapp.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List <String> names = Arrays.asList("adam","anna","jack","jenny");
    @Test
    public void transformUsingMap(){
        Flux <String> nameFlux = Flux.fromIterable(names)
                .map(s->s.toUpperCase())
                .log();
        

        StepVerifier.create(nameFlux)
                .expectNext("ADAM","ANNA","JACK","JENNY")
                .verifyComplete();
    }
    @Test
    public void trasformUsingMapLength(){
        Flux<Integer> nameFlux = Flux.fromIterable(names)
                .map(s->s.length())
                .repeat()
                .log();
        StepVerifier.create(nameFlux)
                .expectNext(4,4,4,5,4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void trasformUsingMapLengthFilter(){
        Flux < String > nameFlux = Flux.fromIterable(names)
                .filter(s->s.length()>4)
                .map(s->s.toUpperCase())
                .log();
        StepVerifier.create(nameFlux)
                .expectNext("JENNY")
                .verifyComplete();
    }

    @Test
    public void trasformUsingFlatMap(){
        Flux<String> nameFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap(s ->Flux.fromIterable(convertToList(s)) )
                .log();
        StepVerifier.create(nameFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
    @Test
    public void trasformUsingFlatMapAndParallel(){
        Flux<String> nameFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
                .flatMap(s -> s.map(this::convertToList).subscribeOn(parallel()) )
                .flatMap(s->Flux.fromIterable(s))
                .log();
        StepVerifier.create(nameFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
    @Test
    public void trasformUsingFlatMapAndParallelOrderly(){
        Flux<String> nameFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
//                .concatMap(s -> s.map(this::convertToList).subscribeOn(parallel()) )
                .flatMapSequential(s -> s.map(this::convertToList).subscribeOn(parallel()) )
                .flatMap(s->Flux.fromIterable(s))
                .log();
        StepVerifier.create(nameFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"newValue");

    }
}
