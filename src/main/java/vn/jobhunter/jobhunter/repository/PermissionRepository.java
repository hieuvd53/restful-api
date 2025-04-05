package vn.jobhunter.jobhunter.repository;

import vn.jobhunter.jobhunter.domain.auth.Permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // dung And giua la spring tu hieu
    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
}
