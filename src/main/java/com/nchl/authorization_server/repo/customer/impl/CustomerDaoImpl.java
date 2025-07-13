package com.nchl.authorization_server.repo.customer.impl;

import com.nchl.authorization_server.model.dto.customer.CustomerDetails;
import com.nchl.authorization_server.repo.customer.CustomerQueryBuilder;
import com.nchl.authorization_server.repo.customer.ICustomerDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDaoImpl implements ICustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CustomerDetails> findCustomerDetailsByCustomerId(String custID) {
        String query = CustomerQueryBuilder.fetchCustomerDetailsByCustomerId;
        List<Object[]> result = entityManager
                .createNativeQuery(query)
                .setParameter("custID", custID)
                .getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        }
        Object[] row = result.get(0);

        CustomerDetails customer = CustomerDetails.builder()
                .custId((String) row[0])
                .custName((String) row[1])
                .emailId((String) row[2])
                .virtualPrivateAddress((String) row[3])
                .roleCode((String) row[4])
                .password((String) row[5])
                .build();
        return Optional.of(customer);
    }
}
