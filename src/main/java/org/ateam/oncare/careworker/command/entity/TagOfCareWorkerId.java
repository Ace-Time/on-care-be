package org.ateam.oncare.careworker.command.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TagOfCareWorkerId implements Serializable {
    private Long careWorkerId;
    private Long tagId;
}
