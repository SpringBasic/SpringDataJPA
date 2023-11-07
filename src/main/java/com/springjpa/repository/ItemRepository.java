package com.springjpa.repository;

import com.springjpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
