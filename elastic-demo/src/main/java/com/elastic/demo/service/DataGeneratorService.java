package com.elastic.demo.service;

import com.elastic.demo.entity.User;
import com.elastic.demo.entity.UserAddress;
import com.elastic.demo.repository.UserAddressRepository;
import com.elastic.demo.repository.UserRepository;
import com.elastic.demo.util.DataUtils;
import com.elastic.demo.util.MaritalStatus;
import com.elastic.demo.util.Profession;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created on 16/September/2021 By Author Eresh, Gorantla
 **/
@Service
@RequiredArgsConstructor
public class DataGeneratorService {

	private final UserRepository userRepository;
	private final UserAddressRepository userAddressRepository;
	private final DataUtils dataUtils;

	public void loadData(Integer limit) {
		List<User> users = IntStream.range(0, limit).mapToObj(v -> generateUser()).collect(Collectors.toList());
		userRepository.saveAll(users);

		List<String> ids = users.stream().map(User::getId).collect(Collectors.toList());
		List<UserAddress> userAddresses = ids.stream().flatMap(id -> generateUserAddresses(id).stream()).collect(Collectors.toList());
		userAddressRepository.saveAll(userAddresses);
	}

	private List<UserAddress> generateUserAddresses(String id) {
		Integer size = ThreadLocalRandom.current().nextInt(0, 5);
		return IntStream.range(0, size).mapToObj(i -> generateAddress(id)).collect(Collectors.toList());
	}

	private UserAddress generateAddress(String id) {
		UserAddress userAddress = new UserAddress();
		userAddress.setAddress1(RandomStringUtils.randomAlphabetic(25));
		userAddress.setAddress2(RandomStringUtils.randomAlphabetic(15));
		userAddress.setUserId(id);
		userAddress.setCity("Hyderabad");
		userAddress.setId(UUID.randomUUID().toString());
		userAddress.setLandmark(RandomStringUtils.randomAlphabetic(15));
		userAddress.setState("Telangana");
		userAddress.setZipCode(RandomStringUtils.randomNumeric(6));
		userAddress.setStreet(RandomStringUtils.randomAlphabetic(20));
		userAddress.setCreatedOn(dataUtils.generateCreatedOn());
		return userAddress;
	}

	private User generateUser() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setFirstName(dataUtils.generateRandomFirstName());
		user.setLastName(dataUtils.generateRandomLastName());
		user.setCity("Hyderabad");
		user.setCountry("India");
		user.setIncome(dataUtils.generateRandomIncome());
		user.setDateOfBirth(dataUtils.generateRandomDateOfBirth());
		user.setMaritalStatus(MaritalStatus.generateMaritalStatus().getStatus());
		user.setNativeResident(dataUtils.generateRandomIsResident());
		Integer numberOfSiblings = dataUtils.generateNumberOfSiblings();
		user.setNumberOfSiblings(numberOfSiblings);
		user.setSiblings(dataUtils.generateSiblings(numberOfSiblings));
		user.setPoint(dataUtils.generateRandomPoint());
		user.setProfession(Profession.generateRandomProfession().getValue());
		user.setUniqueId(dataUtils.generateRandomUniqueId());
		user.setDateOfBirth(dataUtils.generateRandomDateOfBirth());
		user.setCreatedOn(dataUtils.generateCreatedOn());
		user.setMobileNumber(dataUtils.generateMobileNumber());
		user.setPhrase(dataUtils.generatePhrase());
		return user;
	}
}