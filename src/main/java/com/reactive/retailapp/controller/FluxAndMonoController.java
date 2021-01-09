package com.reactive.retailapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
public class FluxAndMonoController {
    List <Integer> myList= Arrays.asList(1,2,3,4,5,1,2,3,4,5,6,7,8,9,10);

    @GetMapping("/flux")
    public Flux <Integer> returnFlux(){
        return Flux.just(1,2,3,4,5,6,7,8,9,10)
                .log();
    }
    @GetMapping(value = "/fluxstream",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux <Integer> returnFluxStream(){
        return Flux.fromIterable(myList)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }
    @PostMapping(value = "/addfluxstream")
    public void addFluxStream(@RequestBody IntArray x){
        System.out.println("-------------->>>>----   recieved" + x.getMyList());
        for(Integer i :x.getMyList())
            System.out.println(i);
        List<Integer> new1 = Arrays.asList(x.getMyList());
       myList.addAll(new1);
    }

}
