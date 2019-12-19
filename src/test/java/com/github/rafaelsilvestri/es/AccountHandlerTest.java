package com.github.rafaelsilvestri.es;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.rafaelsilvestri.es.model.Account;
import com.github.rafaelsilvestri.es.repository.AccountElasticRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Test class uses TestContainers to run tests against Elasticsearch
 *
 * @author Rafael Silvestri
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class AccountHandlerTest {

  @Container
  public static ElasticsearchContainer container =
      new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.5.0");

  @Autowired
  AccountElasticRepository repository;

  @BeforeAll
  public static void before() {
    System.setProperty("spring.data.elasticsearch.client.reactive.endpoints",
        container.getContainerIpAddress() + ":" + container.getMappedPort(9200));
  }

  @Test
  @Order(1)
  void shouldAddNewAccount() {
    Account account = new Account();
    account.setFirstName("Rafael");
    account.setLastName("Silvestri");
    account.setAge(35);
    account.setGender('M');
    account.setEmployer("Foo");
    account.setEmail("foo@bar.com");
    account.setAddress("Lorem ipsum");
    account.setCity("Criciuma");
    account.setState("Santa Catarina");
    account.setAccountNumber(666);
    account.setBalance(5000.00);

    Mono<Account> accountSaved = repository.save(account);
    assertNotNull(accountSaved.block());
  }

  @Test
  @Order(2)
  void shouldReturnAtLeastOneWhenFindAllIsCalled() {
    Flux<Account> accounts = repository.findAll();
    assertTrue(accounts.count().block() > 0);
  }

  @Test
  @Order(3)
  void shouldReturnOneEntryWhenAgeIs35() {
    Flux<Account> results = repository.findByAge(35);
    assertTrue(results.count().block() == 1);
  }

  @Test
  @Order(3)
  void shouldReturnOneEntryWhenAccountNumberIs666() {
    Flux<Account> results = repository.findByAccountNumber(666);
    assertTrue(results.count().block() == 1);
  }

  @Test
  @Order(4)
  void shouldReturnOneEntryWhenNameIsRafael() {
    Flux<Account> accounts = repository.findByFirstName("Rafael");
    assertEquals(1, accounts.count().block());
  }

  @Test
  @Order(5)
  void findByFirstNameUsingCustomQueryShouldReturnOneEntryWhenNameIsRafael() {
    Flux<Account> accounts = repository.findByFirstNameUsingCustomQuery("Rafael");
    assertEquals(1, accounts.count().block());
  }

 /* @Test
  @Order(9999)
  void shouldDeleteAccountNumber666() {
    Disposable accounts = repository.findByAccountNumber(666)
        .flatMap(a -> Mono.just(a.getId()))
        .subscribe(System.out::println);
//
//    StepVerifier
//        .create(deleted)
//        .expectNextMatches(profile -> profile.getEmail().equalsIgnoreCase(test))
//        .verifyComplete();

    Flux<Account> results = repository.findByAccountNumber(666);
    assertTrue(results.count().block() == 0);
  }*/

}