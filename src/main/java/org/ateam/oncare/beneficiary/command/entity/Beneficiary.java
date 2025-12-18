    package org.ateam.oncare.beneficiary.command.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "beneficiary")
    public class Beneficiary {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "gender", nullable = false, length = 1)
        private String gender; // "M", "F"

        @Column(name = "birthdate", nullable = false)
        private LocalDate birthdate;

        @Column(name = "address", nullable = false)
        private String address;

        @Column(name = "phone")
        private String phone;

        @Column(name = "out_of_poket_ratio")
        private Double outOfPoketRatio; // 본인부담금 비율

        @Column(name = "status", nullable = false)
        private Boolean status; // 0:해지, 1:서비스중

        // --- 외래키 ID 직접 매핑 ---

        @Column(name = "potential_customer_id", nullable = false)
        private Long potentialCustomerId;

        @Column(name = "risk_id", nullable = false)
        private Integer riskId;

        @Column(name = "last_counsel_date")   // 최근 상담일 추가
        private LocalDateTime lastCounselDate;
    }