package vn.jobhunter.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.jobhunter.jobhunter.domain.auth.Permission;
import vn.jobhunter.jobhunter.domain.auth.Role;
import vn.jobhunter.jobhunter.repository.PermissionRepository;
import vn.jobhunter.jobhunter.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // check role exist
    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    // đang làm dở
    public Role create(Role r) {
        // check permission
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            // List<Permission> dbPermissions =
            // this.permissionRepository.findById(reqPermissions);
            // r.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(r);
    }
}
