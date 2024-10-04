package com.cloudians.domain.personaldiary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElementCharacter;

public interface FiveElementCharacterRepository extends JpaRepository<FiveElementCharacter, Long> {
    @Query(value = "SELECT * FROM  five_element_character WHERE element_id = :elementId ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<FiveElementCharacter> findRandomCharactersByElementId(@Param("elementId") Long elementId);
}
