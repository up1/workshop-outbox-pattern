package com.example.order.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutBoxRepository extends JpaRepository<OutBoxEntity, Integer> {
}
