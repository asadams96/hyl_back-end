package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.CategoryDao;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryService {


    //************************************************** DAO
    private static CategoryDao categoryDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        CategoryService.categoryDao = categoryDao;
    }


    //************************************************** METHODES
    public static Category getCategoryById(long idCategory) {
        Optional<Category> optCategory = categoryDao.findById(idCategory);
        if (optCategory.isPresent()) return optCategory.get();
        else throw new CustomNotFoundException("La catégorie ayant pour id '"+idCategory+"' est introuvable.");
    }

    public Category addChildCategory(String name, Long idParent, Long idUser) {
        Category category = new Category();
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent( idParent != null ? getCategoryById(idParent) : null);
        CustomValidator.validate(category, Category.AddChildValidation.class);
        return categoryDao.save(category);
    }

    public static boolean checkAtomicName(String name) {
        return categoryDao.findByName(name).isEmpty();
    }
}
