package vn.jobhunter.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.jobhunter.jobhunter.domain.auth.Permission;
import vn.jobhunter.jobhunter.domain.auth.Role;
import vn.jobhunter.jobhunter.service.RoleService;
import vn.jobhunter.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // create permission
    @PostMapping("/roles")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role r)
            throws IdInvalidException {
        if (this.roleService.existsByName(r.getName())) {
            throw new IdInvalidException("role ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.roleService.create(r));
    }
}
