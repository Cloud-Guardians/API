package com.cloudians.domain.publicdiary.entity.report;

import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class PublicDiaryCommentReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "public_diary_comment_report_id")
    Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_comment_id")
    private PublicDiaryComment reportedComment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    private String customReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(nullable = false)
    private boolean isRead;

    @Builder
    private PublicDiaryCommentReport(User reporter, PublicDiaryComment reportedComment, ReportReason reason, String customReason, ReportStatus status, boolean isRead) {
        this.reporter = reporter;
        this.reportedComment = reportedComment;
        this.reason = reason;
        this.customReason = customReason;
        this.status = ReportStatus.PENDING;
        this.isRead = false;
    }
}