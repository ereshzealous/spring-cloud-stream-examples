package com.elastic.demo.model;

import com.elastic.demo.entity.User;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created on 20/September/2021 By Author Eresh, Gorantla
 **/
@Data
public class WSUsersResponse extends RestResponse {
	private List<User> users = Collections.emptyList();
}