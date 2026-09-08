package com.ecommerce.api.repository;

import com.ecommerce.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o " +
           "JOIN FETCH o.user " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product " +
           "WHERE o.user.id = :userId")
    List<Order> findOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT o FROM Order o " +
           "JOIN FETCH o.user " +
           "LEFT JOIN FETCH o.orderItems oi " +
           "LEFT JOIN FETCH oi.product")
    List<Order> findAllOrdersWithItemsAndUser();

    @Query("""
    SELECT o FROM Order o
    JOIN FETCH o.user
    LEFT JOIN FETCH o.orderItems oi
    LEFT JOIN FETCH oi.product
    WHERE o.id = :orderId
    """)
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

}