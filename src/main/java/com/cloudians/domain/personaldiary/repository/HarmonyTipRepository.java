package com.cloudians.domain.personaldiary.repository;

import com.cloudians.domain.personaldiary.entity.analysis.HarmonyTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HarmonyTipRepository extends JpaRepository<HarmonyTip, Long> {
    @Query(value = "(SELECT * FROM harmony_tip WHERE activity_tag = :tag1 ORDER BY RAND() LIMIT 1) " +
            "UNION ALL " +
            "(SELECT * FROM harmony_tip WHERE activity_tag = :tag2 ORDER BY RAND() LIMIT 1) " +
            "UNION ALL " +
            "(SELECT * FROM harmony_tip WHERE activity_tag = :tag3 ORDER BY RAND() LIMIT 1)",
            nativeQuery = true)
    List<HarmonyTip> findRandomTipsByTags(@Param("tag1") String tag1,
                                          @Param("tag2") String tag2,
                                          @Param("tag3") String tag3);
}
