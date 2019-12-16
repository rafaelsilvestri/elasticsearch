package com.github.rafaelsilvestri.es.config;

import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Hold a elastic scheduler instance. It creates a thread pool to execute to work.
 *
 * @author Rafael Silvestri
 */
@Configuration
public class SchedulerConfig {

    public static final Scheduler SCHEDULER = Schedulers.elastic();

}
