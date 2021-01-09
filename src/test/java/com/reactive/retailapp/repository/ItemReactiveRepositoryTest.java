package com.reactive.retailapp.repository;

import com.reactive.retailapp.document.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;
    List < Item > itemList = Arrays.asList(new Item(null, "samsung tv", 78.0)
            , new Item(null, "LG tv", 56.00)
            , new Item(null, "iphone 12", 500.90)
            , new Item(null, "zealot headset", 5.09)
            , new Item("234", "iphone 12", 500.90));

    @Before
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("insert item as : " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById("234").log())
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("iphone 12"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("LG tv").log("findItemByDescription : "))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Google Home Min44", 30.00);
        Mono < Item > saveItem = itemReactiveRepository.save(item);

        StepVerifier.create(saveItem.log())
                .expectSubscription()
                .expectNextMatches(item1 -> (item1.getId() != null && item1.getDescription().equals("Google Home Min44")))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        Double newPrice = 450.00;

        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("LG tv")
        .map(item -> {
            item.setPrice(newPrice);
                    return item;
        }).flatMap(item -> itemReactiveRepository.save(item));

        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches(item1 -> (item1.getPrice() == 450.00))
                .verifyComplete();

    }

    @Test
    public void deleteItem(){
        Mono<Void> deletedItem = itemReactiveRepository.findById("234")
                .map(Item::getId)
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
//                .expectNextMatches(item -> itemReactiveRepository.findById("234").equals(null))
                .verifyComplete();
    }
}