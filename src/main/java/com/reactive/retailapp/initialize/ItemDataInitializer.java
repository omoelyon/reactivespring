package com.reactive.retailapp.initialize;

import com.reactive.retailapp.document.Item;
import com.reactive.retailapp.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetUp();
    }
    public List<Item> data(){
        return Arrays.asList(new Item(null, "data samsung tv", 78.0)
                , new Item(null, "data LG tv", 56.00)
                , new Item(null, "iphone 12 data", 500.90)
                , new Item(null, "zealot headset data", 5.09)
                , new Item("ABC", "iphone 12 data", 500.90));
    }

    private void initialDataSetUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> {
                    System.out.println("Item inserted from commandline " + item);
                });

    }
}
