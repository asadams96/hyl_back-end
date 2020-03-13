package com.hyl.itemapi.controller;

import com.hyl.itemapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "item")
public class ItemController {


    //************************************************** SERVICES
    private CategoryService categoryService;
    private ItemService itemService;
    private PictureService pictureService;
    private SubItemService subItemService;
    private TrackingSheetService trackingSheetService;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public ItemController(CategoryService categoryService, ItemService itemService, PictureService pictureService,
                          SubItemService subItemService, TrackingSheetService trackingSheetService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.pictureService = pictureService;
        this.subItemService = subItemService;
        this.trackingSheetService = trackingSheetService;
    }


    //************************************************** GET
    @GetMapping("/check-category-name")
    public void checkAtomicCategoryName() {
        // TODO
    }

    @GetMapping("/check-item-name")
    public void checkAtomicItemName() {
        // TODO
    }

    @GetMapping("/check-sub-ref")
    public void checkAtomicSubItemRef() {
        // TODO
    }

    @GetMapping("/")
    public void getMainCategory() {
        // TODO
    }


    //************************************************** POST
    @PostMapping("/add-child-category")
    public void addChildCategory() {
        // TODO
    }

    @PostMapping("/add-parent-category")
    public void addParentCategory() {
        // TODO
    }

    @PostMapping("/add-item")
    public void addItem() {
        // TODO
    }

    @PostMapping("/add-subitem")
    public void addSubItem() {
        // TODO
    }

    @PostMapping("/add-tracking-sheet")
    public void addTrackingSheet() {
        // TODO
    }


    //************************************************** PATCH
    @PatchMapping("/rename-category")
    public void renameCategory() {
        // TODO
    }

    @PatchMapping("/move-category")
    public void moveCategory() {
        // TODO
    }

    @PatchMapping("/rename-item")
    public void renameItem() {
        // TODO
    }

    @PatchMapping("/move-item")
    public void moveItem() {
        // TODO
    }

    @PatchMapping("/rename-subitem")
    public void renameSubItem() {
        // TODO
    }

    @PatchMapping("/edit-subitem")
    public void editSubItem() {
        // TODO
    }

    //************************************************** DELETE
    @DeleteMapping("/delete-category")
    public void deleteCategory() {
        // TODO
    }

    @DeleteMapping("/delete-item")
    public void deleteItem() {
        // TODO
    }

    @DeleteMapping("/delete-subitem")
    public void deleteSubItem() {
        // TODO
    }

    @DeleteMapping("/delete-tracking-sheets")
    public void deleteTrackingSheets() {
        // TODO
    }
}
