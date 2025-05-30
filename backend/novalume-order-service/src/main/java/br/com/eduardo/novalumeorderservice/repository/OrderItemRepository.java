package br.com.eduardo.novalumeorderservice.repository;

import br.com.eduardo.novalumeorderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
