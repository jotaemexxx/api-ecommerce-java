package com.ecommerce.api.repository;

import com.ecommerce.api.model.Order;
import com.ecommerce.api.model.OrderItem;
import com.ecommerce.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
