package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.dto.RegistrationSellerDto;
import ru.n1fex.markeazy.entity.Product;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.exception.LoginAlreadyExistsException;
import ru.n1fex.markeazy.repository.ProductRepository;
import ru.n1fex.markeazy.repository.SellerRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    public Optional<Seller> findByLogin(String login) {
        return sellerRepository.findByLogin(login);
    }

    public Optional<Seller> findById(Long id) {
        return sellerRepository.findById(id);
    }

    public List<Product> getProducts(Seller seller, Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return productRepository.findBySellerOrderBySeller(seller, pageable);
    }

    public Seller createNewSeller(RegistrationSellerDto sellerDto) {
        Optional<Seller> existedSeller = sellerRepository.findByLogin(sellerDto.getLogin());
        if (existedSeller.isPresent()) {
            throw new LoginAlreadyExistsException(
                    String.format("Продавец с логином %s уже существует", sellerDto.getLogin())
            );
        }

        Seller seller = new Seller();
        seller.setLogin(sellerDto.getLogin());
        seller.setName(sellerDto.getName());
        seller.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        seller.setRegistrationDate(new Date());
        seller.setRoles(java.util.Set.of(roleService.getSellerRole()));

        return sellerRepository.save(seller);
    }
}
