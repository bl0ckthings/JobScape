package com.jobscape.companyservice.service;
import java.net.URI;
import java.util.Locale;
import java.util.List;
import java.util.Optional;
import com.jobscape.companyservice.client.CompanyClient;
import com.jobscape.companyservice.dto.CompanyDto;
import com.jobscape.companyservice.dto.CompanySelectionDto;
import com.jobscape.companyservice.service.mapper.CompanyMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

        private final CompanyClient companyClient;
        private final CompanyMapper companyMapper;

        public List<CompanyDto> getCompanies() {
                return companyClient.getCompanies();
        }

        public CompanyDto getCompanyById(Long id) {
                return companyClient.getCompanyById(id);
        }

        public CompanyDto createCompany(CompanyDto company) {
                if (company.getDomain() == null || company.getDomain().isBlank()) {
                        company.setDomain(null);
                } else {
                        company.setDomain(normalizeDomain(company.getDomain()));
                }
                return companyClient.createCompany(company);
        }

        public Optional<CompanyDto> findCompanyByDomain(String domain) {
                try {
                        return Optional.of(companyClient.getCompanyByDomain(normalizeDomain(domain)));
                } catch (FeignException.NotFound exception) {
                        return Optional.empty();
                }
        }

        public void deleteCompany(Long id) {
                companyClient.deleteCompany(id);
        }

        public CompanyDto resolveCompany(CompanySelectionDto companySelectionDto) {
                if (companySelectionDto == null || companySelectionDto.getName() == null
                        || companySelectionDto.getName().isBlank()) {
                        throw new IllegalArgumentException("Company name is required");
                }

                if (companySelectionDto.getDomain() != null && !companySelectionDto.getDomain().isBlank()) {
                        Optional<CompanyDto> company = findCompanyByDomain(companySelectionDto.getDomain());
                        if (company.isPresent()) {
                                return company.get();
                        }
                }
                CompanyDto companyDto = companyMapper.toDto(companySelectionDto);
                return createCompany(companyDto);
        }

        private String normalizeDomain(String rawDomain) {
                if (rawDomain == null || rawDomain.isBlank()) {
                        throw new IllegalArgumentException("Domain is required");
                }

                String input = rawDomain.trim();
                String url = input.contains("://") ? input : "https://" + input;

                try {
                        URI uri = URI.create(url);

                        if (!"http".equalsIgnoreCase(uri.getScheme())
                                && !"https".equalsIgnoreCase(uri.getScheme())) {
                                throw new IllegalArgumentException("Invalid domain");
                        }

                        String host = uri.getHost();
                        if (host == null || host.isBlank() || uri.getUserInfo() != null) {
                                throw new IllegalArgumentException("Invalid domain");
                        }

                        host = host.toLowerCase(Locale.ROOT);
                        if (host.endsWith(".")) {
                                host = host.substring(0, host.length() - 1);
                        }
                        if (host.startsWith("www.")) {
                                host = host.substring(4);
                        }
                        if (!host.contains(".")) {
                                throw new IllegalArgumentException("Invalid domain");
                        }

                        return host;
                } catch (IllegalArgumentException exception) {
                        throw new IllegalArgumentException("Invalid domain", exception);
                }
        }
}
