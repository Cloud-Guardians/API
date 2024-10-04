package com.cloudians.domain.statistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.statistics.entity.Collection;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findListByUserEmail(String userEmail);
}
