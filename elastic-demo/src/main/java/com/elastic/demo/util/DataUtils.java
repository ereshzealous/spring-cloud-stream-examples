package com.elastic.demo.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created on 16/September/2021 By Author Eresh, Gorantla
 **/
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class DataUtils {

	private List<String> prebuiltPhrases = null;

	@PostConstruct
	public void init() {
		prebuiltPhrases = new ArrayList<>();
		prebuiltPhrases = IntStream.range(0, 100000).mapToObj(v -> RandomStringUtils.randomAlphanumeric(
				ThreadLocalRandom.current().nextInt(5, 12)))
				.collect(Collectors.toList());
	}

	private final Supplier<String> mobileSupplier =
			() -> String.format("%01d%01d%01d%01d%01d%01d%01d0%01d%01d%01d", ThreadLocalRandom.current().nextInt(6, 9),
			                    ThreadLocalRandom.current().nextInt(9), ThreadLocalRandom.current().nextInt(9),
			                    ThreadLocalRandom.current().nextInt(9), ThreadLocalRandom.current().nextInt(9),
			                    ThreadLocalRandom.current().nextInt(9), ThreadLocalRandom.current().nextInt(9),
			                    ThreadLocalRandom.current().nextInt(9), ThreadLocalRandom.current().nextInt(9),
			                    ThreadLocalRandom.current().nextInt(9));
	private final Supplier<Integer> randomNumber = () -> ThreadLocalRandom.current().nextInt(5, 10);
	private final Supplier<Integer> siblingsNumber = () -> ThreadLocalRandom.current().nextInt(0, 4);
	private final Supplier<Long> incomeSupplier = () -> ThreadLocalRandom.current().nextLong(100000, 1000000);

	public String generateMobileNumber() {
		return Stream.generate(mobileSupplier).limit(1).findFirst().orElse(null);
	}

	public String generateRandomFirstName() {
		return RandomStringUtils.randomAlphabetic(Stream.generate(randomNumber).limit(1).findFirst().orElse(10));
	}

	public String generateRandomLastName() {
		return RandomStringUtils.randomAlphabetic(Stream.generate(randomNumber).limit(1).findFirst().orElse(8));
	}

	public String generateRandomUniqueId() {
		return RandomStringUtils.randomAlphanumeric(16).toUpperCase();
	}

	public Integer generateNumberOfSiblings() {
		return Stream.generate(siblingsNumber).limit(1).findFirst().orElse(0);
	}

	public List<String> generateSiblings(Integer limit) {
		return IntStream.range(0, limit).mapToObj(l -> generateRandomFirstName() + " "  + generateRandomLastName()).collect(Collectors.toList());
	}

	public Double[] generateRandomPoint() {
		return new Double[]{randomPoint(75.000001, 79.500001), randomPoint(15.000001, 19.000001)};
	}

	public static Double randomPoint(Double min, Double max) {
		double x = (Math.random() * ((max - min) + 1)) + min;
		double xRounded = Math.round(x * 100000.0) / 100000.0;
		return xRounded;
	}

	public Long generateRandomIncome() {
		return Stream.generate(incomeSupplier).limit(1).findFirst().orElse(100000L);
	}

	public Boolean generateRandomIsResident() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public LocalDate generateRandomDateOfBirth() {
		int year = ThreadLocalRandom.current().nextInt(1949, 2020);
		int month = ThreadLocalRandom.current().nextInt(1, 12);
		int day = ThreadLocalRandom.current().nextInt(1, 28);
		return LocalDate.of(year, month, day);
	}

	public LocalDateTime generateCreatedOn() {
		LocalDateTime localDate = LocalDateTime.now();
		int year = ThreadLocalRandom.current().nextInt(2018, 2020);
		int month = ThreadLocalRandom.current().nextInt(1, 12);
		int day = ThreadLocalRandom.current().nextInt(1, 28);
		int hour =  ThreadLocalRandom.current().nextInt(0, 23);
		int minutes =  ThreadLocalRandom.current().nextInt(1, 59);
		int seconds =  ThreadLocalRandom.current().nextInt(1, 59);
		int nanoSeconds =  ThreadLocalRandom.current().nextInt(100, 900);
		return LocalDateTime.of(year, month, day, hour, minutes, seconds, nanoSeconds);
	}

	public String generatePhrase() {
		return prebuiltPhrases.get(ThreadLocalRandom.current().nextInt(0, 99999)) + " " +
				RandomStringUtils.randomAlphanumeric(ThreadLocalRandom.current().nextInt(4, 10)) + " " +
				RandomStringUtils.randomAlphanumeric(ThreadLocalRandom.current().nextInt(5, 8)) + " " +
				prebuiltPhrases.get(ThreadLocalRandom.current().nextInt(0, 99999));
	}
}