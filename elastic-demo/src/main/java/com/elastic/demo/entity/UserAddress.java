package com.elastic.demo.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created on 16/September/2021 By Author Eresh, Gorantla
 **/
@Document(indexName = "user_address", shards = 2, createIndex = false)
@Data
public class UserAddress {
	@Id
	private String id;
	private String userId;
	private String address1;
	private String address2;
	private String street;
	private String landmark;
	private String city;
	private String state;
	private String zipCode;
	@JsonDeserialize(converter = User.LongToLocalDateTimeConverter.class)
	private LocalDateTime createdOn;

	public static class LongToLocalDateTimeConverter extends StdConverter<Long, LocalDateTime> {
		public LocalDateTime convert(final Long value) {
			return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
	}
 }