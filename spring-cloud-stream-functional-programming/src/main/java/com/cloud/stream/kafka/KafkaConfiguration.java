package com.cloud.stream.kafka;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created on 26/August/2021 By Author Eresh, Gorantla
 **/
@Configuration
@Slf4j
public class KafkaConfiguration {
	Random random = new Random();

	@Bean
	public Supplier<Flux<Integer>> fizzBuzzProducer(){
		return () -> Flux.interval(Duration.ofSeconds(5)).map(value -> random.nextInt(10000 - 1) + 1).log();
	}

	@Bean
	public Function<Flux<Integer>, Flux<String>> fizzBuzzProcessor(){
		return longFlux -> longFlux
				.map(i -> evaluateFizzBuzz(i))
				.log();
	}

	@Bean
	public Consumer<String> fizzBuzzConsumer(){
		return (value) -> log.info("Consumer Received : " + value);
	}

	private String evaluateFizzBuzz(Integer value) {
		if (value % 15 == 0) {
			return "FizzBuzz";
		} else if (value % 5 == 0) {
			return "Buzz";
		} else if (value % 3 == 0) {
			return "Fizz";
		} else {
			return String.valueOf(value);
		}
	}

}