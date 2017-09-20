package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.model.Category;
import pl.kflorczyk.onlineshopbackend.repository.CategoriesRepository;

import java.util.List;

@Service
public class CategoriesService {

    private CategoriesRepository categoriesRepository;

    public CategoriesService(@Autowired CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> getCategories() {
        return categoriesRepository.findAll();
    }
}
