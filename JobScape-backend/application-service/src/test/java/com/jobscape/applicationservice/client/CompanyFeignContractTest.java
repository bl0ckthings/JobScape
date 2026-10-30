package com.jobscape.applicationservice.client;

import com.jobscape.applicationservice.dto.company.CompanyDto;
import com.jobscape.applicationservice.dto.company.CompanySelectionDto;
import com.jobscape.applicationservice.dto.contact.ContactDto;
import feign.Feign;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CompanyFeignContractTest {
    private Request lastRequest;

    @Test void clientsShareCompanyServiceWithDistinctContexts() {
        FeignClient contacts = ContactClient.class.getAnnotation(FeignClient.class);
        FeignClient companies = CompanyClient.class.getAnnotation(FeignClient.class);
        assertEquals("company-service", contacts.name());
        assertEquals("company-service", companies.name());
        assertNotEquals(contacts.contextId(), companies.contextId());
    }

    @Test void contactReadsMatchCompanyServiceRoutes() {
        ContactClient client = client(ContactClient.class);
        client.getContactsByCompanyId(7L, 3L);
        assertRequest("GET", "/contacts/company/7?userId=3");
        client.getContactsByUserId(3L);
        assertRequest("GET", "/contacts/user/3");
        client.getContactById(8L, 3L);
        assertRequest("GET", "/contacts/8?userId=3");
    }

    @Test void contactWritesCarryOwnerSeparatelyFromBody() {
        ContactClient client = client(ContactClient.class);
        ContactDto contact = new ContactDto();
        client.createContact(contact, 3L);
        assertRequest("POST", "/contacts/create?userId=3");
        assertNotNull(lastRequest.body());
        client.updateContact(8L, 3L, contact);
        assertRequest("PUT", "/contacts/update/8?userId=3");
        client.deleteContact(8L, 3L);
        assertRequest("DELETE", "/contacts/delete/8?userId=3");
    }

    @Test void companyReadsMatchCompanyServiceRoutesAndEncodeDomain() {
        CompanyClient client = client(CompanyClient.class);
        client.getCompanies();
        assertRequest("GET", "/companies");
        client.getCompanyById(7L);
        assertRequest("GET", "/companies/7");
        client.getCompanyByDomain("example.com");
        assertRequest("GET", "/companies/domain?domain=example.com");
    }

    @Test void companyWritesMatchCompanyServiceRoutes() {
        CompanyClient client = client(CompanyClient.class);
        client.createCompany(new CompanyDto());
        assertRequest("POST", "/companies/create");
        client.resolveCompany(new CompanySelectionDto());
        assertRequest("POST", "/companies/resolve");
        client.deleteCompany(7L);
        assertRequest("DELETE", "/companies/delete?id=7");
    }

    private <T> T client(Class<T> type) {
        return Feign.builder().contract(new SpringMvcContract())
                .encoder((body, bodyType, template) -> template.body("{}"))
                .decoder((response, bodyType) -> null)
                .client((request, options) -> {
                    lastRequest = request;
                    return Response.builder().request(request).status(200).reason("OK")
                            .headers(Map.of()).body("{}", java.nio.charset.StandardCharsets.UTF_8).build();
                }).target(type, "http://company-service");
    }

    private void assertRequest(String method, String path) {
        assertEquals(method, lastRequest.httpMethod().name());
        assertEquals("http://company-service" + path, lastRequest.url());
    }
}
