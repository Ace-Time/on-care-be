package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personal_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", nullable = false, length = 255)
    private String tag;

}