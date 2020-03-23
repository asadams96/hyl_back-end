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
    private static String categoryMaxAuthorizedDepth;


    //************************************************** DAO
    private static CategoryDao categoryDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        CategoryService.categoryDao = categoryDao;
    }


    //************************************************** SETTER
    @Value("${hyl.constraint.category.depth}")
    public void setCategoryMaxAuthorizedDepth(String categoryMaxAuthorizedDepth) {
        CategoryService.categoryMaxAuthorizedDepth = categoryMaxAuthorizedDepth;
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

    public static void moveCategory(long idCategory, Long idCategoryDest) {
        Category category = getCategoryById(idCategory);
        Category categoryDest = idCategoryDest != null && idCategoryDest != 0 ? getCategoryById(idCategoryDest) : null;

        CustomValidator.validate(category, Category.OwnerValidation.class);
        if (categoryDest != null) CustomValidator.validate(categoryDest, Category.OwnerValidation.class);

        if( isHierarchicalLink(category, categoryDest) )
            throw new CustomBadRequestException("Déplacement impossible: les catégories ont un lien de parenté.");

        category.setCategoryParent(categoryDest);

        if (idCategoryDest != null && idCategoryDest != 0) {
            categoryDest.getCategories().add(category);
            categoryDest = categoryDao.save(categoryDest);
            checkCategoryDepth(categoryDest);
        } else {
            category = categoryDao.save(category);
            checkCategoryDepth(category);
        }
    }

    public static Category addChildCategory(String name, Long idParent, Long idUser) {
        Category category = new Category();
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent( idParent != null && idParent!= 0 ? getCategoryById(idParent) : null);
        CustomValidator.validate(category, Category.AddChildValidation.class);
        category = categoryDao.save(category);
        checkCategoryDepth(category);
        return category;
    }

    public static Category addParentCategory(String name, long idChild, Long idUser) {
        Category category = new Category();
        Category oldParentCategory = getCategoryById(idChild);
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent(oldParentCategory.getCategoryParent());
        category.setCategories(new ArrayList<>());
        category.getCategories().add(oldParentCategory);
        oldParentCategory.setCategoryParent(category);
        category = categoryDao.save(category);
        checkCategoryDepth(category);
        return category;
    }

    public static void deleteCategory(long id) {
        Category category = getCategoryById(id);

        CustomValidator.validate(category, Category.OwnerValidation.class);

        Category parentCategory = category.getCategoryParent();

        category.getCategories().forEach(category1 -> {
            category1.setCategoryParent(parentCategory);
            if (parentCategory != null) parentCategory.getCategories().add(category1);
            else categoryDao.save(category1);
        });

        category.getItems().forEach(item -> {
            item.setCategory(parentCategory);
            if (parentCategory != null) parentCategory.getItems().add(item);
            else ItemService.save(item);
        });
        if (parentCategory != null) categoryDao.save(parentCategory);

        categoryDao.delete(category);
    }

    private static void checkCategoryDepth(Category pCategory) {
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

        if (pCategory != null) {
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

        }
    }

    public static boolean checkAtomicName(String name) {
        return categoryDao.findByName(name).isEmpty();
    }

    private static boolean isHierarchicalLink(Category category1, Category category2) {
        class Interne {
            void recursivity (Category category1, Category category2) {
                if (category1 != null && category1.getCategories() != null) {
                    for (Category category : category1.getCategories()) {
                        if (category.equals(category2)) throw new RuntimeException();
                        recursivity(category, category2);
                    }
                }
            }
        }

        if ( category1.getCategoryParent() == category2) {
            return true;
        }
        try {
            new Interne().recursivity(category1, category2);
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }
}
