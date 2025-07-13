package com.nchl.authorization_server.repo.customer;

import com.nchl.authorization_server.model.dto.customer.CustomerDetails;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ICustomerDao {

    Optional<CustomerDetails> findCustomerDetailsByCustomerId(@Param("email") String email);

}
