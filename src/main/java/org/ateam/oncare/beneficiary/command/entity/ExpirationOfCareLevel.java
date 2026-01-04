package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expiration_of_care_level")
public class ExpirationOfCareLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "expired_section", nullable = false)
    private Integer expiredSection; // 1:90일전, 2:60일전, 3:45일전

    @Column(name = "outbound_status", nullable = false, length = 1)
    private String outboundStatus; // "Y"/"N", default 'N'

    @Column(name = "extends_status", length = 1)
    private String extendsStatus; // "Y"/"N", default 'N'

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "emp_id", nullable = false)
    private Integer empId; // 담당 직원 ID
}