package vn.jobhunter.jobhunter.service;

import org.springframework.data.domain.Pageable;

import vn.jobhunter.jobhunter.domain.Company;
import vn.jobhunter.jobhunter.domain.dto.ResultPaginationDTO;

public interface CompanyService {

    Company handleSave(Company company);

    void handleDelete(long id);

    ResultPaginationDTO getAllCompanies(Pageable pageable);

    Company handleUpdate(Company updateCompany);
}
