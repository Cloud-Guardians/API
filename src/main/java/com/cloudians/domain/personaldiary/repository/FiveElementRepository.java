package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiveElementRepository extends JpaRepository<FiveElement, Long> {
    FiveElement findByName(String name);
}
