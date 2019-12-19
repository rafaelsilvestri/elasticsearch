package com.github.rafaelsilvestri.es;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import com.github.rafaelsilvestri.es.config.SchedulerConfig;
import com.github.rafaelsilvestri.es.model.Account;
import com.github.rafaelsilvestri.es.repository.AccountElasticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Account endpoints handler
 *
 * @author Rafael Silvestri
 */
@Component
public class AccountHandler {

  private static final Logger log = LoggerFactory.getLogger(AccountHandler.class);

  private AccountElasticRepository repository;

  public AccountHandler(AccountElasticRepository repository) {
    this.repository = repository;
  }

  public Mono<ServerResponse> save(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Account.class)
        .map(repository::save)
        .flatMap(account -> ok().body(account, Account.class))
        .doOnError(ex -> log.error("Oops, something went wrong!!!", ex))
        .doOnSuccess(e -> log.info("Success!!! {}", e.statusCode()))
        .subscribeOn(SchedulerConfig.SCHEDULER);
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    return noContent().build(repository.deleteById(id))
        .switchIfEmpty(notFound().build());
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
