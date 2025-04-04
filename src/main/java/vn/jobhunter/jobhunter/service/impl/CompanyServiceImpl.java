package vn.jobhunter.jobhunter.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.jobhunter.jobhunter.domain.Company;
import vn.jobhunter.jobhunter.domain.response.Meta;
import vn.jobhunter.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobhunter.jobhunter.repository.CompanyRepository;
import vn.jobhunter.jobhunter.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company handleSave(Company company) {
        return this.companyRepository.save(company);
    }

    @Override
    public void handleDelete(long id) {
        this.companyRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO getAllCompanies(Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta meta = new Meta();
        meta.setPage(pageCompany.getNumber() + 1);
        meta.setPageSize(pageCompany.getSize());
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getNumberOfElements());

        rs.setMeta(meta);
        rs.setResult(pageCompany.getContent());
        return rs;

    }

    @Override
    public Company handleUpdate(Company updateCompany) {
        Optional<Company> companyOptional = this.companyRepository.findById(updateCompany.getId());
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setName(updateCompany.getName());
            company.setAddress(updateCompany.getAddress());
            company.setDescription(updateCompany.getDescription());
            company.setLogo(updateCompany.getLogo());

            return this.companyRepository.save(company);
        }

        return null;
    }

}
