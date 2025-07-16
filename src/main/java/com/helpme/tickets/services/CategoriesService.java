package com.helpme.tickets.services;

import com.helpme.tickets.exceptions.CategoryAlreadyExistsException;
import com.helpme.tickets.model.Category;
import com.helpme.tickets.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriesService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category create(String name) {
        Optional< Category> categoryExists = this.categoryRepository.findByName(name);
        if (categoryExists.isPresent()) {
            throw new CategoryAlreadyExistsException();
        }

        Category category = new Category();
        category.setName(name);

        return this.categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Optional<Category> findById(UUID id) {
        return this.categoryRepository.findById(id);
    }

}
