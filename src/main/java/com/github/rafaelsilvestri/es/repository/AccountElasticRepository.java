package com.github.rafaelsilvestri.es.repository;

import com.github.rafaelsilvestri.es.model.Account;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Elasticsearch reactive repository
 *
 * @author Rafael Silvestri
 */
@Repository
public interface AccountElasticRepository extends ReactiveElasticsearchRepository<Account, String> {

    Flux<Account> findByFirstName(String name);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"firstname\": \"?0\"}}]}}")
    Flux<Account> findByFirstNameUsingCustomQuery(String name);
}
