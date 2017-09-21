package pl.kflorczyk.onlineshopbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kflorczyk.onlineshopbackend.model.CategoryLogic;
import pl.kflorczyk.onlineshopbackend.model.CategoryView;
import pl.kflorczyk.onlineshopbackend.repository.CategoriesViewRepository;

import java.util.List;

@Service
public class CategoriesService {

    private CategoriesViewRepository categoriesViewRepository;

    public CategoriesService(@Autowired CategoriesViewRepository categoriesViewRepository) {
        this.categoriesViewRepository = categoriesViewRepository;
    }

    public List<CategoryView> getCategories() {
        return categoriesViewRepository.findAll();
    }
}
