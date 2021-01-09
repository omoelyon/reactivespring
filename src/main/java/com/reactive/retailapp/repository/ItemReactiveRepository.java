package com.reactive.retailapp.repository;

import com.reactive.retailapp.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository< Item, String> {

    Flux <Item> findByDescription(String description);
}
