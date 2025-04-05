package vn.jobhunter.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.jobhunter.jobhunter.domain.auth.Permission;
import vn.jobhunter.jobhunter.domain.response.Meta;
import vn.jobhunter.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobhunter.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // get list permission
    public ResultPaginationDTO fetchAllPermissions(Pageable pageable) {
        Page<Permission> permissionPage = this.permissionRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getNumberOfElements());

        rs.setMeta(meta);
        List<Permission> permissions = permissionPage.getContent()
                .stream().map(item -> new Permission(
                // item.getId(),
                // item.getName(),
                // item.getApiPath(),
                // item.getMethod(),
                // item.getModule(), item.getRoles()
                )).collect(Collectors.toList());
        rs.setResult(permissions);
        return rs;
    }

    // check permission exist
    public boolean isPermissionExist(Permission p) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(),
                p.getApiPath(),
                p.getMethod());
    }

    public Permission createNewPermission(Permission p) {
        return this.permissionRepository.save(p);
    }

    public Permission updatePermission(Permission p) {
        Permission permission = this.permissionRepository.findById(p.getId()).orElse(null);
        if (permission != null) {
            permission.setName(p.getName());
            permission.setApiPath(p.getApiPath());
            permission.setModule(p.getModule());
            permission.setMethod(p.getMethod());
            return this.permissionRepository.save(permission);
        }
        return null;
    }

    public Permission fetchPermissionById(Long id) {
        Permission permission = this.permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            return permission;
        }
        return null;
    }
}
