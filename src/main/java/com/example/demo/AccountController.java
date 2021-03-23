package com.example.demo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.demo.Account.PhoneResponse;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// indicates that the data returned by each method will be written straight 
// into the response body instead of rendering a template
@RestController
public class AccountController{
    private final AccountRepo repository;
    private final ArrayList<PhoneResponse> responseBuffer = new ArrayList<>();

    AccountController(AccountRepo repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/contacts")
    List<Account> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/contacts")
    Account create(@RequestBody Account created) {
        return repository.save(created);
    }

    // Single item
    @GetMapping("/contacts/{id}")
    EntityModel<Account> one(@PathVariable Long id) {
        Account account = repository.findById(id)
        .orElseThrow(() -> new AccountNotFoundException(id));
        return addLinks(account, id);
    }

    @PutMapping("/contact/{id}")
    EntityModel<Account> replace(@RequestBody Account update, @PathVariable Long id) {
        Account account = repository.findById(id)
        .map(old -> {
            old.update(update);
            old.id = id;
            return repository.save(old);
        })
        .orElseGet(() -> {
            update.id = id;
            return repository.save(update);
        });

        return addLinks(account, id);
    }

    @DeleteMapping("/contacts/{id}")
    void delete(@PathVariable Long id) {
        repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        repository.deleteById(id);
    }

    @GetMapping("/contacts/call-list")
    CollectionModel<PhoneResponse> list() {
        responseBuffer.clear();
        List<Account> all = repository.findAll();
        for(Account a : all) {
            PhoneResponse p = a.phone();
            if(p != null) {
                responseBuffer.add(p);
            }
        }
        responseBuffer.sort(Comparator.comparing(a -> (a.name.last + a.name.first)));
        return CollectionModel.of(
            responseBuffer,
            linkTo(methodOn(AccountController.class).all()).withRel("contacts")
        );
    }

    <T> EntityModel<T> addLinks(T value, long id) {
        return EntityModel.of(
            value,
            linkTo(methodOn(AccountController.class).one(id)).withSelfRel(),
            linkTo(methodOn(AccountController.class).all()).withRel("contacts"),
            linkTo(methodOn(AccountController.class).list()).withRel("contacts/call-list")
        );
    }
}


