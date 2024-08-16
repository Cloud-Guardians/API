package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FiveElementRepository extends JpaRepository<FiveElement, Long> {
    Optional<FiveElement> findByName(String name);
}
