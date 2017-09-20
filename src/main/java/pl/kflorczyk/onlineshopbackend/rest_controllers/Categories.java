package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.kflorczyk.onlineshopbackend.model.Category;
import pl.kflorczyk.onlineshopbackend.services.CategoriesService;

@RestController
public class Categories {

    @Autowired
    private CategoriesService categoriesService;

    @RequestMapping(path = "/categories", method = RequestMethod.GET)
    public String getCategories() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        ArrayNode categories = node.putArray("categories");

        for(Category category : categoriesService.getCategories()) {
            ObjectNode jsonNode = categories.addObject();
            jsonNode.put("name", category.getName());
            jsonNode.put("id", category.getID());
            jsonNode.put("parent_id", category.getParent() == null ? -1 : category.getParent().getID());
        }

        return node.toString();
    }
}
