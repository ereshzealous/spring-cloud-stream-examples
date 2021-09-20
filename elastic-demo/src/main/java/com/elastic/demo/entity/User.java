package com.elastic.demo.entity;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created on 16/September/2021 By Author Eresh, Gorantla
 **/
@Document(indexName = "user", shards = 2, createIndex = false)
@Data
public class User {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String uniqueId;
	private String country;
	private String city;
	private String mobileNumber;
	private Double[] point = {0.0, 0.0};
	private String maritalStatus;
	private Integer numberOfSiblings;
	private List<String> siblings;
	private String profession;
	private Long income;
	private String phrase;
	private Boolean nativeResident = true;
	private LocalDate dateOfBirth;
	private LocalDateTime createdOn;
}