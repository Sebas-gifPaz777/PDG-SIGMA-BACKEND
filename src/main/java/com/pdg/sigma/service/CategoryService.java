package com.pdg.sigma.service;

import java.util.List;

import com.pdg.sigma.domain.Category;

public interface CategoryService extends GenericService<Category, Integer> {
    List<Category> findByCourseId(Integer courseId);
}
