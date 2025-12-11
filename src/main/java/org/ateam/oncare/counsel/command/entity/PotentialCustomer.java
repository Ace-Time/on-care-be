package org.ateam.oncare.counsel.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "potential_customer")
public class PotentialCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "gender", length = 1)
    private String gender; // M:남자, F:여자

    @Column(name = "birthdate")
    private LocalDate birthdate; // DATETIME이지만 생년월일이므로 LocalDate 권장

    @Column(name = "address")
    private String address;

    @Column(name = "willingness", length = 1)
    private String willingness; // 'N: 가입의사 없음, Y: 초기값'

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}