package vn.jobhunter.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.jobhunter.jobhunter.domain.Company;
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
    public List<Company> getAllCompanies(Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);
        return pageCompany.getContent();

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
