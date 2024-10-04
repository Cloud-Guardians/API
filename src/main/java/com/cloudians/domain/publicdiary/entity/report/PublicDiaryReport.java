package com.cloudians.domain.publicdiary.entity.report;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
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
public class PublicDiaryReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "public_diary_report_id")
    Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "public_diary_id")
    private PublicDiary reportedDiary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    private java.lang.String customReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(nullable = false)
    private boolean isRead;

    @Builder
    private PublicDiaryReport(User reporter, PublicDiary reportedDiary, ReportReason reason, java.lang.String customReason, ReportStatus status, boolean isRead) {
        this.reporter = reporter;
        this.reportedDiary = reportedDiary;
        this.reason = reason;
        this.customReason = customReason;
        this.status = ReportStatus.PENDING;
        this.isRead = false;
    }

    public void changeReadStatus(boolean isRead) {
        this.isRead = isRead;
    }

    public void changeStatus(ReportStatus status) {
        this.status = status;
    }
}
