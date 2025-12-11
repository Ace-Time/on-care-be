package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_significant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Significant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "significant_category_id", nullable = false)
    private Long significantCategoryId;

}