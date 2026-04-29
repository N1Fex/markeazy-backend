package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.entity.Role;
import ru.n1fex.markeazy.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

    public Role getSellerRole() {
        return roleRepository.findByName("ROLE_SELLER").get();
    }

}
