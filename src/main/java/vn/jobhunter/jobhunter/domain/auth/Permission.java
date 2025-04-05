package vn.jobhunter.jobhunter.domain.auth;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.jobhunter.jobhunter.domain.AbstractAuditingEntity;
import vn.jobhunter.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name khong the trong")
    private String name;
    @NotBlank(message = "apiPath khong the trong")
    private String apiPath;
    @NotBlank(message = "method khong the trong")
    private String method;
    @NotBlank(message = "module khong the trong")
    private String module;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    @PrePersist
    public void handleBeforeCreate() {
        String currentUser = SecurityUtil.getCurrentUserLogin().orElse("");
        this.setCreatedBy(currentUser);
        this.setCreatedDate(Instant.now());
    }
}
