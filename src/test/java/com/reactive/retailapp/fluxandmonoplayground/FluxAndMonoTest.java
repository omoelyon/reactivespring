package com.reactive.retailapp.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class FluxAndMonoTest {
    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("spring", "Spring boot", "reactive spring boot")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("after error"))
                .log();
        stringFlux.subscribe(System.out::println, (e)->System.out.println("------> exception is : "+e)
                ,()-> System.out.println("completed"));
    }
    @Test
    public void fluxTestWithoutError(){
        Flux<String> stringFlux = Flux.just("spring", "Spring boot", "reactive spring boot").log();

        StepVerifier.create(stringFlux)
                .expectNext("spring")
                .expectNext("Spring boot")
                .expectNext("reactive spring boot")
                .verifyComplete();
    }
    @Test
    public void testIp(){
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }
    }

    @Test
    public void addBigDecimal(){
        BigDecimal premium = new BigDecimal(50);
        premium.add(BigDecimal.valueOf(50));
        Mono <BigDecimal> value = Mono.just(premium.add(BigDecimal.valueOf(50))).log();
        StepVerifier.create(value)
                .expectNext(BigDecimal.valueOf(100))
                .verifyComplete();
    }
}
