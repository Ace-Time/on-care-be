// 서비스 유형 마스터
package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_service_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 방문요양, 방문간호, 방문목욕
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    // 시급 (33,000 / 50,000 등)
    @Column(name = "money", nullable = false)
    private Integer money;

    // 생성자
    public MServiceType(String name, Integer money) {
        this.name = name;
        this.money = money;
    }
}