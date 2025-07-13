package com.nchl.authorization_server.service;

import com.nchl.authorization_server.model.dto.customer.CustomerDetails;

public interface ICustomerDetailService {
    CustomerDetails getCustomerDetail(String custId);
}
