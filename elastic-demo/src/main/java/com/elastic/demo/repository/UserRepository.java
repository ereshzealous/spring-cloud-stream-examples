package com.elastic.demo.repository;

import com.elastic.demo.entity.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created on 16/September/2021 By Author Eresh, Gorantla
 **/
public interface UserRepository extends ElasticsearchRepository<User, String> {

}