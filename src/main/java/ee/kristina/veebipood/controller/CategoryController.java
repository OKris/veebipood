package ee.kristina.veebipood.controller;

import ee.kristina.veebipood.entity.Category;
import ee.kristina.veebipood.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {


    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("categories")
    public ResponseEntity<List<Category>> getCategories () {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping("categories")
    public ResponseEntity<List<Category>> addCategory(@RequestBody Category category) {
        if(category.getId()!=null) {
            throw new RuntimeException("Cannot add category with id");
        }
        categoryRepository.save(category);
        return ResponseEntity.status(201).body(categoryRepository.findAll()); // 201 - created
    }


    // localhost:8080/products/{id}
    @DeleteMapping("categories/{id}")
    public ResponseEntity<List<Category>> deleteCategory (@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(categoryRepository.findAll());
    }
}
