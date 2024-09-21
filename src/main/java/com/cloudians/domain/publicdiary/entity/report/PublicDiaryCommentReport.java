package com.cloudians.domain.publicdiary.entity.report;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "public_diary_comment_report_id"))
public class PublicDiaryCommentReport extends Report {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_comment_id")
    private PublicDiaryComment reportedComment;

}