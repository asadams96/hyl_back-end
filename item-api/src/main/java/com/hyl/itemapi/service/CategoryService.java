package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.CategoryDao;
import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {


    //************************************************** BEAN
    @Value("${hyl.constraint.category.depth}")
    private String categoryMaxAuthorizedDepth;


    //************************************************** DAO
    private static CategoryDao categoryDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        CategoryService.categoryDao = categoryDao;
    }


    //************************************************** METHODES
    public static Category getMainCategory(long idUser) {
        Category category = new Category();
        category.setCategories(categoryDao.findAllByIdUserAndCategoryParentIsNull(idUser));
        category.setItems(ItemService.getItemsWihtoutCategoryByIdUser(idUser));
        return category;
    }

    public static Category getCategoryById(long idCategory) {
        Optional<Category> optCategory = categoryDao.findById(idCategory);
        if (optCategory.isPresent()) return optCategory.get();
        else throw new CustomNotFoundException("La catégorie ayant pour id '"+idCategory+"' est introuvable.");
    }

    public static void renameCategory(long idCategory, String name) {
        Category category = getCategoryById(idCategory);
        if (!category.getName().equals(name)) {
            category.setName(name);
            CustomValidator.validate(category, Category.UpdateValidation.class);
            categoryDao.save(category);
        }
    }

    public Category addChildCategory(String name, Long idParent, Long idUser) {
        Category category = new Category();
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent( idParent != null ? getCategoryById(idParent) : null);
        CustomValidator.validate(category, Category.AddChildValidation.class);
        category = categoryDao.save(category);
        this.checkCategoryDepth(category);
        return category;
    }

    public Category addParentCategory(String name, long idChild, Long idUser) {
        Category category = new Category();
        Category oldParentCategory = getCategoryById(idChild);
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent(oldParentCategory.getCategoryParent());
        category.setCategories(new ArrayList<>());
        category.getCategories().add(oldParentCategory);
        oldParentCategory.setCategoryParent(category);
        category = categoryDao.save(category);
        this.checkCategoryDepth(category);
        return category;
    }

    public void checkCategoryDepth(Category pCategory) {
        class Interne {
            private int checkByRecursivity (Category pCategory, int counter) {
                if (pCategory.getCategories() != null) {
                    for (Category category : pCategory.getCategories()) {
                        counter ++;
                        counter = this.checkByRecursivity(category, counter);
                    }
                }
                return counter;
            }
        }
        if (pCategory == null) return;
        else {
            Interne interne = new Interne();
            boolean hasParent = true;
            while (hasParent) {
                if (pCategory.getCategoryParent() != null) pCategory = pCategory.getCategoryParent();
                else hasParent = false;
            }
            if (pCategory.getCategories() != null) {
                int authorizedDepth = Integer.parseInt(categoryMaxAuthorizedDepth);
                for (Category category : pCategory.getCategories()) {
                    int count = 1;
                    count = interne.checkByRecursivity(category, count);
                    if (count > authorizedDepth) {
                        throw new CustomBadRequestException("La profondeur de catégorie autorisé a été atteint.");
                    }
                }
            }
            return;
        }
    }

    public static boolean checkAtomicName(String name) {
        return categoryDao.findByName(name).isEmpty();
    }
}
