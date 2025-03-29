package vn.jobhunter.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.jobhunter.jobhunter.domain.Company;

public interface CompanyService {

    Company handleSave(Company company);

    void handleDelete(long id);

    List<Company> getAllCompanies(Pageable pageable);

    Company handleUpdate(Company updateCompany);
}
