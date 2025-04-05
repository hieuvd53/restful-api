package vn.jobhunter.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.jobhunter.jobhunter.domain.auth.Permission;
import vn.jobhunter.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobhunter.jobhunter.service.PermissionService;
import vn.jobhunter.jobhunter.util.annotation.ApiMessage;
import vn.jobhunter.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // create permission
    @PostMapping("/permissions")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission p)
            throws IdInvalidException {
        if (this.permissionService.isPermissionExist(p)) {
            throw new IdInvalidException("permission ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.permissionService.createNewPermission(p));
    }

    // update permission
    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission p)
            throws IdInvalidException {
        if (this.permissionService.isPermissionExist(p)) {
            throw new IdInvalidException("permission ton tai!");
        }
        if (this.permissionService.fetchPermissionById(p.getId()) == null) {
            throw new IdInvalidException("permission id =" + p.getId() + "chua ton tai!");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.permissionService.updatePermission(p));
    }

    // fetch all permission
    @GetMapping("/permissions")
    @ApiMessage("fetch all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {

        String sCurrent = currentOptional.orElse("");
        String sPageSize = pageSizeOptional.orElse("");

        int currentPage = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return ResponseEntity.ok().body(this.permissionService.fetchAllPermissions(pageable));
    }
}
