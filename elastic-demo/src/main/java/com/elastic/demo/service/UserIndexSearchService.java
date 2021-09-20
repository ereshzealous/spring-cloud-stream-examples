package com.elastic.demo.service;

import com.elastic.demo.entity.User;
import com.elastic.demo.entity.UserAddress;
import com.elastic.demo.model.WSMultiIndexResponse;
import com.elastic.demo.model.WSUsersResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 20/September/2021 By Author Eresh, Gorantla
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserIndexSearchService {

	private final HighLevelRestClient highLevelRestClient;
	private final ObjectMapper objectMapper;

	public WSUsersResponse searchMatchPhrase(String phrase, Integer offset, Integer limit) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("phrase", phrase));
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse multiSearchQuery(String query, Integer offset, Integer limit, Boolean prefixPhraseEnabled) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(query, "firstName", "lastName", "uniqueId", "mobileNumber");
		if (prefixPhraseEnabled) {
			multiMatchQuery.type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
		}
		sourceBuilder.query(multiMatchQuery);
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse queryKeywordTerm(String query, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.termQuery("profession.keyword", query));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse queryTerm(String query, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.termQuery("profession", query.toLowerCase()));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse queryTerms(List<String> queries, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.termsQuery("profession", queries.stream().map(data -> data.toLowerCase()).collect(Collectors.toList())));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse queryBoolWithMust(String profession, String mobileNumber, String maritalStatus, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		BoolQueryBuilder queryBuilders = new BoolQueryBuilder();
		BoolQueryBuilder queryBuilders1 = new BoolQueryBuilder();
		queryBuilders1.must(QueryBuilders.matchQuery("profession", profession));
		queryBuilders.filter(queryBuilders1);

		BoolQueryBuilder queryBuilders2 = new BoolQueryBuilder();
		queryBuilders2.must(QueryBuilders.wildcardQuery("mobileNumber", "*" + mobileNumber + "*"));
		queryBuilders.filter(queryBuilders2);

		BoolQueryBuilder queryBuilders3 = new BoolQueryBuilder();
		queryBuilders3.must(QueryBuilders.matchQuery("maritalStatus", maritalStatus));
		queryBuilders.filter(queryBuilders3);

		sourceBuilder.query(queryBuilders);
		searchRequest.source(sourceBuilder);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse queryBoolWithShould(String profession, String mobileNumber, String maritalStatus, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		BoolQueryBuilder queryBuilders = new BoolQueryBuilder();
		queryBuilders.should(QueryBuilders.matchQuery("profession", profession));
		queryBuilders.should(QueryBuilders.wildcardQuery("mobileNumber", "*" + mobileNumber + "*"));
		queryBuilders.should(QueryBuilders.matchQuery("maritalStatus", maritalStatus));
		sourceBuilder.query(queryBuilders);
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse wildcardSearch(String query, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		BoolQueryBuilder queryBuilders = new BoolQueryBuilder();
		queryBuilders.should(QueryBuilders.wildcardQuery("firstName", "*" + query + "*"));
		queryBuilders.should(QueryBuilders.wildcardQuery("lastName", "*" + query + "*"));
		queryBuilders.should(QueryBuilders.wildcardQuery("uniqueId", "*" + query + "*"));
		sourceBuilder.query(queryBuilders);
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse regExpSearch(String query, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.regexpQuery("siblings", "e[a-z]*h"));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse simpleQueryStringSearch(String query, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		Map<String, Float> map = new HashMap<>();
		map.put("firstName", 1.0F);
		map.put("lastName", 2.0F);
		sourceBuilder.query(QueryBuilders.simpleQueryStringQuery(query).fields(map));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse searchIncomeRange(Integer lowerLimit, Integer upperLimit, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.rangeQuery("income").gte(lowerLimit).lte(upperLimit));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse searchDateRange(String fromDate, String toDate, Integer offset, Integer limit) {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.rangeQuery("dateOfBirth").gte(fromDate).lte(toDate));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSUsersResponse queryGeographyPoint(Double lon, Double lat, Integer offset, Integer limit) throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.from(offset);
		sourceBuilder.size(limit);
		sourceBuilder.query(QueryBuilders.geoDistanceQuery("point").point(lat, lon).distance("1km"));
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = highLevelRestClient.postSearchQueries(searchRequest);
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return extractUserResponse(searchResponse);
	}

	public WSMultiIndexResponse multiIndexSearch(String userId) {
		MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

		SearchRequest userIndexSearch = new SearchRequest();
		userIndexSearch.indices("user");
		SearchSourceBuilder userIndexSourceBuilder = new SearchSourceBuilder();
		userIndexSourceBuilder.query(QueryBuilders.matchQuery("id.keyword", userId));
		userIndexSearch.source(userIndexSourceBuilder);
		multiSearchRequest.add(userIndexSearch);

		SearchRequest userAddressIndexSearch = new SearchRequest();
		userAddressIndexSearch.indices("user_address");
		SearchSourceBuilder userAddressIndexSourceBuilder = new SearchSourceBuilder();
		userAddressIndexSourceBuilder.query(QueryBuilders.matchQuery("userId.keyword", userId));
		userAddressIndexSearch.source(userAddressIndexSourceBuilder);
		multiSearchRequest.add(userAddressIndexSearch);

		MultiSearchResponse response = highLevelRestClient.postMSearch(multiSearchRequest);
		return extractMSearchResponse(response);
	}

	private WSUsersResponse extractUserResponse(SearchResponse searchResponse) {
		WSUsersResponse response = new WSUsersResponse();
		SearchHits searchHits = searchResponse.getHits();
		response.setTotalRecords(searchHits.getTotalHits().value);
		SearchHit[] searchHitsArray = searchResponse.getHits().getHits();
		List<User> users = new ArrayList<>();
		for (SearchHit hit : searchHitsArray) {
			users.add(objectMapper.convertValue(hit.getSourceAsMap(), User.class));
		}
		response.setUsers(users);
		return response;
	}

	private WSMultiIndexResponse extractMSearchResponse(MultiSearchResponse searchResponse) {
		WSMultiIndexResponse response = new WSMultiIndexResponse();
		SearchResponse userSearchResponse = searchResponse.getResponses()[0].getResponse();
		SearchHits userSearchHits = userSearchResponse.getHits();
		SearchHit userSearchHit = userSearchHits.getHits()[0];
		response.setUser(objectMapper.convertValue(userSearchHit.getSourceAsMap(), User.class));

		SearchResponse userAddressSearchResponse = searchResponse.getResponses()[1].getResponse();
		SearchHits userAddressSearchHits = userAddressSearchResponse.getHits();
		List<UserAddress> userAddresses = new ArrayList<>();
		for (SearchHit hit : userAddressSearchHits) {
			userAddresses.add(objectMapper.convertValue(hit.getSourceAsMap(), UserAddress.class));
		}
		response.setUserAddresses(userAddresses);
		return response;
	}
}