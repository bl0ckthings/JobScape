package com.jobscape.entitymanager.service;

import com.jobscape.entitymanager.dto.ContactDto;
import com.jobscape.entitymanager.exception.ResourceNotFoundException;
import com.jobscape.entitymanager.model.Company;
import com.jobscape.entitymanager.model.Contact;
import com.jobscape.entitymanager.model.User;
import com.jobscape.entitymanager.repository.CompanyRepository;
import com.jobscape.entitymanager.repository.ContactRepository;
import com.jobscape.entitymanager.repository.UserRepository;
import com.jobscape.entitymanager.service.mapper.ContactMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    @Transactional(readOnly = true)
    public ContactDto getById(Long id) {
        return contactMapper.toDto(findContact(id));
    }

    @Transactional(readOnly = true)
    public List<ContactDto> getAll() {
        return contactRepository.findAll().stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContactDto> getByCompanyId(Long companyId) {
        return contactRepository.findByCompanyId(companyId).stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Transactional
    public ContactDto create(ContactDto dto) {
        Company company = resolveCompany(dto);
        User user = resolveUser(dto.getUserId());
        return contactMapper.toDto(contactRepository.save(contactMapper.toEntity(dto, company, user)));
    }

    @Transactional
    public ContactDto update(Long id, ContactDto dto) {
        Contact contact = findContact(id);
        if (dto.getName() != null) {
            contact.setName(dto.getName());
        }
        if (dto.getPhone() != null) {
            contact.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            contact.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            contact.setRole(dto.getRole());
        }
        if (dto.getCompany() != null && dto.getCompany().getId() != null) {
            contact.setCompany(resolveCompany(dto));
        }
        if (dto.getUserId() != null) {
            contact.setUser(resolveUser(dto.getUserId()));
        }
        return contactMapper.toDto(contactRepository.save(contact));
    }

    @Transactional
    public void delete(Long id) {
        contactRepository.delete(findContact(id));
    }

    private Contact findContact(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", id));
    }

    private Company resolveCompany(ContactDto dto) {
        if (dto.getCompany() == null || dto.getCompany().getId() == null) {
            return null;
        }
        Long companyId = dto.getCompany().getId();
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", companyId));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
