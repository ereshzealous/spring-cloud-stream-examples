package com.elastic.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * Created on 20/September/2021 By Author Eresh, Gorantla
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class HighLevelRestClient {

	private final RestHighLevelClient restHighLevelClient;

	@SneakyThrows
	public SearchResponse postSearchQueries(SearchRequest searchRequest) {
		log.info("Search JSON query: {}", searchRequest.source().toString());
		return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}

	@SneakyThrows
	public MultiSearchResponse postMSearch(MultiSearchRequest multiSearchRequest) {
		log.info("Search JSON query: {}", multiSearchRequest.requests().toString());
		return restHighLevelClient.msearch(multiSearchRequest, RequestOptions.DEFAULT);
	}
}