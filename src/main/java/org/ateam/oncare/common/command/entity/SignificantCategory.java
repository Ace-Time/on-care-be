package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_significant_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignificantCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name; // 1. 렌탈성사도움, 2. 문의해결도움, 3. 컴플레인해결도움, 4. 해지상담도움

}