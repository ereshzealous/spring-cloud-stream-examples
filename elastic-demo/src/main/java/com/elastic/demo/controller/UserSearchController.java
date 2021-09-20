package com.elastic.demo.controller;

import com.elastic.demo.model.RestResponse;
import com.elastic.demo.service.UserIndexSearchService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created on 20/September/2021 By Author Eresh, Gorantla
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserSearchController {

	private final UserIndexSearchService userIndexSearchService;

	@GetMapping("/search/phrase")
	public ResponseEntity<RestResponse> searchPhrase(@RequestParam("query") String query,
	                                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                 @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws IOException {
		return ResponseEntity.ok(userIndexSearchService.searchMatchPhrase(query, offset, limit));
	}

	@GetMapping("/search/multi")
	public ResponseEntity<RestResponse> searchMulti(@RequestParam("query") String query,
	                                                @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                @RequestParam(value = "offset", defaultValue = "0") Integer offset,
	                                                @RequestParam(value = "prefix_phrase_enabled", defaultValue = "false") Boolean prefixPhraseEnabled) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.multiSearchQuery(query, offset, limit, prefixPhraseEnabled));
	}

	@GetMapping("/search/keyword/term")
	public ResponseEntity<RestResponse> searchKeywordTerm(@RequestParam("query") String query,
	                                                @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.queryKeywordTerm(query, offset, limit));
	}

	@GetMapping("/search/term")
	public ResponseEntity<RestResponse> searchTerm(@RequestParam("query") String query,
	                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                               @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.queryTerm(query, offset, limit));
	}

	@GetMapping("/search/terms")
	public ResponseEntity<RestResponse> searchTerms(@RequestParam("query") List<String> queries,
	                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                               @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.queryTerms(queries, offset, limit));
	}

	@GetMapping("/search/must/bool")
	public ResponseEntity<RestResponse> searchBoolMust(@RequestParam("profession") String profession,
	                                                @RequestParam("mobileNumber") String mobileNumber,
	                                                @RequestParam("maritalStatus") String maritalStatus,
	                                                @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.queryBoolWithMust(profession, mobileNumber, maritalStatus, offset, limit));
	}

	@GetMapping("/search/should/bool")
	public ResponseEntity<RestResponse> searchBoolShould(@RequestParam("profession") String profession,
	                                                @RequestParam("mobileNumber") String mobileNumber,
	                                                @RequestParam("maritalStatus") String maritalStatus,
	                                                @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.queryBoolWithShould(profession, mobileNumber, maritalStatus, offset, limit));
	}

	@GetMapping("/search/wildcard")
	public ResponseEntity<RestResponse> searchBoolShould(@RequestParam("query") String query,
	                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                     @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.wildcardSearch(query, offset, limit));
	}

	@GetMapping("/search/regexp")
	public ResponseEntity<RestResponse> searchRegularExpression(@RequestParam("query") String query,
	                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                     @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.regExpSearch(query, offset, limit));
	}

	@GetMapping("/search/simple")
	public ResponseEntity<RestResponse> searchSimpleQuery(@RequestParam("query") String query,
	                                                            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                            @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.simpleQueryStringSearch(query, offset, limit));
	}

	@GetMapping("/search/income/range")
	public ResponseEntity<RestResponse> searchIncomeRange(@RequestParam("lowerLimit") Integer lowerLimit,
	                                                      @RequestParam("upperLimit") Integer upperLimit,
	                                                      @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                      @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.searchIncomeRange(lowerLimit, upperLimit, offset, limit));
	}

	@GetMapping("/search/date/range")
	public ResponseEntity<RestResponse> searchDateRange(@RequestParam("fromDate") String fromDate,
	                                                      @RequestParam("toDate") String toDate,
	                                                      @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                      @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.searchDateRange(fromDate, toDate, offset, limit));
	}

	@GetMapping("/search/geo_distance")
	public ResponseEntity<RestResponse> searchGeoDistance(@RequestParam("lon") Double longitude,
	                                                      @RequestParam("lat") Double latitude,
	                                                      @RequestParam(value = "limit", defaultValue = "10") Integer limit,
	                                                      @RequestParam(value = "offset", defaultValue = "0") Integer offset) throws Exception {
		return ResponseEntity.ok(userIndexSearchService.queryGeographyPoint(longitude, latitude, offset, limit));
	}
}