package com.reactive.retailapp.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTime {
    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux= Flux.interval(Duration.ofMillis(200))
                .log();

        infiniteFlux.subscribe((element)->System.out.println("value is : "+ element));
        Thread.sleep(10000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> finiteFlux= Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L,1L,2L)
                .verifyComplete();
    }
}
