package vn.jobhunter.jobhunter.domain;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.jobhunter.jobhunter.util.SecurityUtil;
import vn.jobhunter.jobhunter.util.constant.GenderEnum;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractAuditingEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name khong the trong")
    private String name;
    @NotBlank(message = "email khong the trong")
    private String email;
    @NotBlank(message = "password khong the trong")
    private String password;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    @Column(length = 512)
    private String refreshToken;

    @PrePersist
    public void handleBeforeCreate() {
        String currentUser = SecurityUtil.getCurrentUserLogin().orElse("");
        this.setCreatedBy(currentUser);
        this.setCreatedDate(Instant.now());
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        String currentUser = SecurityUtil.getCurrentUserLogin().orElse("");
        this.setLastModifiedBy(currentUser);
        this.setLastModifiedDate(Instant.now());
    }
}
