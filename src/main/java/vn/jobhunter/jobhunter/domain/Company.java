package vn.jobhunter.jobhunter.domain;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.jobhunter.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company extends AbstractAuditingEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name khong duoc trong")
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY) // Trì hoãn tải dữ liệu
    private String description;

    private String address;
    private String logo;

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
