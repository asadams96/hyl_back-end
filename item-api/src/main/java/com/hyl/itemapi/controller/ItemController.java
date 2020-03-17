package com.hyl.itemapi.controller;

import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.validation.MultipartFileListValidation;
import com.hyl.itemapi.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "item")
public class ItemController {


    //************************************************** Logger
    private Logger logger = LoggerFactory.getLogger(ItemController.class);


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
    public boolean checkAtomicCategoryName(@RequestParam String name) {
        return !CategoryService.checkAtomicName(name);
    }

    @GetMapping("/check-item-name")
    public boolean checkAtomicItemName(@RequestParam String name) {
        return !ItemService.checkAtomicName(name);
    }

    @GetMapping("/check-sub-ref")
    public boolean checkAtomicSubItemRef(@RequestParam String reference) {
        return !SubItemService.checkAtomicRef(reference);
    }

    @GetMapping("/")
    public void getMainCategory() {
        // TODO
    }


    //************************************************** POST
    @PostMapping("/add-child-category")
    public Category addChildCategory(@RequestBody HashMap<String, String> hashMap,
                                     @Autowired HttpServletRequest request) {
        String name = hashMap.get("name");
        Long idParent = hashMap.get("idParent") != null ? Long.parseLong(hashMap.get("idParent")) : null;
        return categoryService.addChildCategory(name, idParent, extractIdUserFromHeader(request));
    }

    @PostMapping("/add-parent-category")
    public Category addParentCategory(@RequestBody HashMap<String, String> hashMap,
                                  @Autowired HttpServletRequest request) {
        String name = hashMap.get("name");
        Long idChild = hashMap.get("idChild") != null ? Long.parseLong(hashMap.get("idChild")) : null;
        if (idChild == null) throw new CustomBadRequestException("Le paramètre idChild ne peut pas être null");
        return categoryService.addParentCategory(name, idChild, extractIdUserFromHeader(request));
    }

    @PostMapping("/add-item")
    public Item addItem(@RequestPart(value = "files") List<MultipartFile> files,
                        @RequestPart(value = "data") Object obj,
                        @Autowired HttpServletRequest request) {

        // Validation des images liées au seul et unique subitem obligatoire lors de l'ajout d'un item
        MultipartFileListValidation.validMultipartFileList(files);

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data = (JSONObject) JSONObject.wrap(obj);

        // Appel du service item pour la validation des données puis l'ajout en bdd
        return itemService.addItem(data.optString("name"),
                data.optString("description"),
                data.optLong("idCategory"),
                data.optString("reference"),
                extractIdUserFromHeader(request),
                files);
    }

    @PostMapping("/add-subitem")
    public SubItem addSubItem(@RequestPart(value = "files") List<MultipartFile> files,
                              @RequestPart(value = "data") Object obj) {

        // Validation des images liées au seul et unique subitem obligatoire lors de l'ajout d'un subitem
        MultipartFileListValidation.validMultipartFileList(files);

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data = (JSONObject) JSONObject.wrap(obj);

        // Appel du service subitem pour la validation des données puis l'ajout en bdd
        return subItemService.addSubItem(data.optString("reference"),
                data.optLong("idItem"),
                files);
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


    //************************************************** SHARE
    public static long extractIdUserFromHeader (HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }

        try {
            return Long.parseLong(idUserStr);
        } catch (NumberFormatException e) {
            throw new CustomBadRequestException("L'id de l'utilisateur doit être un nombre.");
        }
    }
}
