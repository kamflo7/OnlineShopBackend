package pl.kflorczyk.onlineshopbackend.rest_controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.kflorczyk.onlineshopbackend.services.CategoryService;

@RestController
public class Categories {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(path = "/categories", method = RequestMethod.GET)
    public String getCategories() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        ArrayNode categories = node.putArray("categories");

//        for(CategoryLogic categoryLogic : categoryService.getCategories()) {
//            ObjectNode jsonNode = categories.addObject();
//            jsonNode.put("name", categoryLogic.getName());
//            jsonNode.put("id", categoryLogic.getID());
//            jsonNode.put("parent_id", categoryLogic.getParent() == null ? -1 : categoryLogic.getParent().getID());
//        }

        return node.toString();
    }
}
