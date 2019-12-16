package com.github.rafaelsilvestri.es.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

/**
 * @author Rafael Silvestri
 */
@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.github.rafaelsilvestri.es.repository")
public class ElasticsearchConfig {

    //
    // Use this block if you need customize elastic client
    //
//    @Bean
//    ReactiveElasticsearchClient client() {
//
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200", "localhost:9291")
//                .build();
//
//        return ReactiveRestClients.create(clientConfiguration);
//    }

}




