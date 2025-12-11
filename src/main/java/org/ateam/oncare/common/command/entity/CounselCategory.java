package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_counsel_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounselCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

}