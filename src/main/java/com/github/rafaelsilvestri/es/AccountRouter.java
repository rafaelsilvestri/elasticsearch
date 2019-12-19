package com.github.rafaelsilvestri.es;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Account Router
 *
 * @author Rafael Silvestri
 */
@Configuration
public class AccountRouter {

  @Bean
  public RouterFunction<ServerResponse> helloRoute(AccountHandler handler) {
    return nest(accept(APPLICATION_JSON),
        route(POST("/v1/es-search"), handler::save)
            .andRoute(PUT("/v1/es-search"),handler::save)
            .andRoute(DELETE("/v1/es-search/{id}"), handler::delete)
            .andRoute(GET("/v1/es-search"), handler::getAll)
            .andRoute(GET("/v1/es-search/{name}"), handler::getByFirstName)
    );
  }
}
