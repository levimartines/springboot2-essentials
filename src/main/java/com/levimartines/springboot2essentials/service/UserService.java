package com.levimartines.springboot2essentials.service;

import com.levimartines.springboot2essentials.exception.ResourceNotFoundException;
import com.levimartines.springboot2essentials.model.User;
import com.levimartines.springboot2essentials.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private final UserRepository repository;

    public User findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User não encontrado."));
    }

    @Transactional
    public User save(User user) {
        user = repository.save(user);
        return user;
    }

    public void delete(Long id) {
        User user = findById(id);
        repository.delete(user);
    }

}
