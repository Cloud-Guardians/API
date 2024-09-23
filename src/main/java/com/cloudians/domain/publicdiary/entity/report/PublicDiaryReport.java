package com.cloudians.domain.publicdiary.entity.report;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "public_diary_report_id"))
public class PublicDiaryReport extends Report {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_id")
    private PublicDiary reportedDiary;

}
