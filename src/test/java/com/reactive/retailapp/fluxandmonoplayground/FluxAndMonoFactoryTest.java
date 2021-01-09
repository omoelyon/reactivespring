package com.reactive.retailapp.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {
    List <String> names = Arrays.asList("adam","anna","jack","jenny");
    @Test
    public void fluxUsingIterable(){
        Flux<String> nameFlux = Flux.fromIterable(names).log();

        StepVerifier.create(nameFlux)
                .expectNext("adam","anna","jack","jenny")
                .verifyComplete();
    }
    @Test
    public void fluxUsingArray(){
        String[] names = new String[]{"adam","anna","jack","jenny"};
        Flux<String> nameFlux = Flux.fromArray(names).log();

        StepVerifier.create(nameFlux)
                .expectNext("adam","anna","jack","jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream(){
        Flux<String> nameFlux = Flux.fromStream(names.stream()).log();

        StepVerifier.create(nameFlux)
                .expectNext("adam","anna","jack","jenny")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Supplier<String> stringSupplier = ()->"adam";

        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(stringMono.log())
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){
        Flux<Integer> integerFlux = Flux.range(1,5);
        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
}
