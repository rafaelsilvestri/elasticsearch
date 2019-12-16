package com.github.rafaelsilvestri.es;

import com.github.rafaelsilvestri.es.config.SchedulerConfig;
import com.github.rafaelsilvestri.es.model.Account;
import com.github.rafaelsilvestri.es.repository.AccountElasticRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

/**
 * Account handler
 *
 * @author Rafael Silvestri
 */
@Component
public class AccountHandler {

    private AccountElasticRepository repository;

    public AccountHandler(AccountElasticRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return repository.findAll()
                .collectList()
                .flatMap(a -> {
                    System.out.println("Executing on thread: " + Thread.currentThread().getName());
                    return ok().body(Mono.just(a), Account.class);
                })
                .onErrorResume(e -> status(INTERNAL_SERVER_ERROR)
                        .body(Mono.just(e.getMessage()), String.class))
                .subscribeOn(SchedulerConfig.SCHEDULER);
    }

    public Mono<ServerResponse> getByFirstName(ServerRequest serverRequest) {
        String name = serverRequest.pathVariable("name");

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Flux<Account> result = repository.findByFirstName(name);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(result, Account.class)
                .switchIfEmpty(notFound);
    }
}
