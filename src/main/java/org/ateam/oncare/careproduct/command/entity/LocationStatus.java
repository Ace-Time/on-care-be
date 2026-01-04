package org.ateam.oncare.careproduct.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_location_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

}