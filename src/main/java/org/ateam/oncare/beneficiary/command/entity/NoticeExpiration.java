package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice_expiration")
public class NoticeExpiration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "notice_date", nullable = false)
    private LocalDateTime noticeDate;

    @Column(name = "memo", length = 2000)
    private String memo;

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "expiration_id", nullable = false)
    private Integer expirationId;

    @Column(name = "emp_id", nullable = false)
    private Integer empId;
}