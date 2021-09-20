package com.elastic.demo.controller;

import com.elastic.demo.service.DataGeneratorService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 16/September/2021 By Author Eresh, Gorantla
 **/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataLoaderController {

	private final DataGeneratorService dataGeneratorService;

	@PostMapping("/load")
	public void loadData(@RequestParam(value = "size", defaultValue = "10") Integer size) {
		dataGeneratorService.loadData(size);
	}
}