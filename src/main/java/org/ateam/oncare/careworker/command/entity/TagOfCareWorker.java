package org.ateam.oncare.careworker.command.entity;

// 2. 엔티티 클래스
import jakarta.persistence.*;
        import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag_of_care_worker")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TagOfCareWorkerId.class)
public class TagOfCareWorker {

    @Id
    @Column(name = "care_worker_id")
    private Long careWorkerId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;
}