package vn.jobhunter.jobhunter.domain.auth;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.jobhunter.jobhunter.domain.AbstractAuditingEntity;
import vn.jobhunter.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends AbstractAuditingEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name khong the trong")
    private String name;
    private String description;
    private boolean active;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "roles" })
    @JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permision_id"))
    private List<Permission> permissions;

    @PrePersist
    public void handleBeforeCreate() {
        String currentUser = SecurityUtil.getCurrentUserLogin().orElse("");
        this.setCreatedBy(currentUser);
        this.setCreatedDate(Instant.now());
    }
}
