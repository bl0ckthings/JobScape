package com.jobscape.entitymanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "applications",
        indexes = {
                @Index(name = "idx_applications_user_status", columnList = "user_id,status"),
                @Index(name = "idx_applications_user_created_at", columnList = "user_id,created_at")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "job_title", nullable = false)
    private String jobTitle;
    private String city;
    private String country;
    private String source;
    private String url;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationStatus status;
    @Column(name = "work_mode")
    private String workMode;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Company company;

    @OneToMany(
            mappedBy = "application",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ApplicationStep> steps = new ArrayList<>();


    public Application(String jobTitle, String city, String country, String source, String url, ApplicationStatus status, String workMode, User user, Company company) {
        this.jobTitle = jobTitle;
        this.city = city;
        this.country = country;
        this.source = source;
        this.url = url;
        this.status = status;
        this.workMode = workMode;
        this.user = user;
        this.company = company;
    }

}
