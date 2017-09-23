package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.model.CategoryView;
import pl.kflorczyk.onlineshopbackend.repository.CategoryViewRepository;

import java.util.List;

@Service
public class CategoryService {

    private CategoryViewRepository categoryViewRepository;

    public CategoryService(@Autowired CategoryViewRepository categoryViewRepository) {
        this.categoryViewRepository = categoryViewRepository;
    }

    public List<CategoryView> getCategories() {
        return categoryViewRepository.findAll();
    }
}
