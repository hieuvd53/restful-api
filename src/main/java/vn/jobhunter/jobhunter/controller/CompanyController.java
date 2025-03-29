package vn.jobhunter.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.jobhunter.jobhunter.domain.Company;
import vn.jobhunter.jobhunter.service.CompanyService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company newCompany) {
        Company company = this.companyService.handleSave(newCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> fetchAllCompanies(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int currentPage = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        List<Company> companies = this.companyService.getAllCompanies(pageable);
        return ResponseEntity.ok().body(companies);
    }

    @PutMapping("/company")
    public ResponseEntity<Company> putMethodName(@Valid @RequestBody Company updateCompany) {
        Company company = this.companyService.handleUpdate(updateCompany);
        return ResponseEntity.ok().body(company);
    }

}
