package com.pdg.sigma.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pdg.sigma.domain.Category;
import com.pdg.sigma.service.CategoryServiceImpl;
import com.pdg.sigma.service.CourseServiceImpl;

@RestController
@RequestMapping("/category")
//@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})

public class CategoryController {

    
    @Autowired
    CategoryServiceImpl categoryService;
    
    @Autowired
    CourseServiceImpl courseService;



    @GetMapping("/getA")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @GetMapping("/course/{courseId}")
    public List<Category> getCategoriesByCourse(@PathVariable Integer courseId) {
        return categoryService.findByCourseId(courseId);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) throws Exception {
        if (category.getCourse() == null || category.getCourse().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }



    @PutMapping("/update/{id}")
    public Category updateCategory(@PathVariable Integer id, @RequestBody Category category) throws Exception {
        category.setId(id);
        return categoryService.update(category);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) throws Exception {
        categoryService.deleteById(id);
        return "Categoría eliminada con éxito";
    }

}
