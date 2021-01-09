package com.reactive.retailapp.handler;

import com.reactive.retailapp.document.Item;
import com.reactive.retailapp.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ItemsHandler {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }


    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemReactiveRepository.findById(id);
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(itemReactiveRepository.findById(id), Item.class)
//                .switchIfEmpty(ServerResponse.notFound().build());
        return itemMono.flatMap(item -> {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(item));
        }).switchIfEmpty(notFound);
    }
}
