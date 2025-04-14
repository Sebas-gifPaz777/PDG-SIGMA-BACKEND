package com.pdg.sigma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdg.sigma.domain.Category;
import com.pdg.sigma.repository.CategoryRepository;
import com.pdg.sigma.repository.CourseRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) throws Exception {
        validate(category);
        return categoryRepository.save(category);
    }   

    @Override
    public Category update(Category category) throws Exception {
        if (category.getId() == null) {
            throw new Exception("El ID de la categoría no puede ser nulo.");
        }
        if (!categoryRepository.existsById(category.getId())) {
            throw new Exception("No se encontró la categoría con ID: " + category.getId());
        }
        validate(category);
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) throws Exception {
        if (category == null || category.getId() == null) {
            throw new Exception("La categoría a eliminar no puede ser nula.");
        }
        categoryRepository.delete(category);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        if (!categoryRepository.existsById(id)) {
            throw new Exception("No se encontró la categoría con ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findByCourseId(Integer courseId) {
        return categoryRepository.findByCourseId(courseId);
    }

    @Override
    public void validate(Category category) throws Exception {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new Exception("El nombre de la categoría es obligatorio.");
        }

        // if (courseRepository.findById(category.getCourse().getId()) == null ) {
        //     throw new Exception("El curso asociado a la categoría no existe.");
        // }
    }


    @Override
    public Long count() {
        return categoryRepository.count();
    }
}
