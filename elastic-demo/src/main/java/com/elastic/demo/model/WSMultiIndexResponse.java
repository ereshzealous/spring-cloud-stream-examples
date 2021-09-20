package com.elastic.demo.model;

import com.elastic.demo.entity.User;
import com.elastic.demo.entity.UserAddress;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created on 20/September/2021 By Author Eresh, Gorantla
 **/
@Data
public class WSMultiIndexResponse extends RestResponse {
	private User user;
	private List<UserAddress> userAddresses = Collections.emptyList();
}