package com.hyl.itemapi.controller;

import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.model.*;
import com.hyl.itemapi.model.validation.MultipartFileListValidation;
import com.hyl.itemapi.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "item")
public class ItemController {


    //************************************************** Logger
    private Logger logger = LoggerFactory.getLogger(ItemController.class);


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
    public Category getMainCategory(@Autowired HttpServletRequest request) {
        return CategoryService.getMainCategory(extractIdUserFromHeader(request));
    }


    //************************************************** POST
    @PostMapping("/add-child-category")
    public Category addChildCategory(@RequestBody HashMap<String, String> hashMap,
                                     @Autowired HttpServletRequest request) {
        String name = hashMap.get("name");
        Long idParent = hashMap.get("idParent") != null ? Long.parseLong(hashMap.get("idParent")) : null;
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        return CategoryService.addChildCategory(name, idParent, extractIdUserFromHeader(request));
    }

    @PostMapping("/add-parent-category")
    public Category addParentCategory(@RequestBody HashMap<String, String> hashMap,
                                  @Autowired HttpServletRequest request) {
        String name = hashMap.get("name");
        Long idChild = hashMap.get("idChild") != null ? Long.parseLong(hashMap.get("idChild")) : null;
        if (idChild == null) throw new CustomBadRequestException("Le paramètre idChild ne peut pas être null");
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        return CategoryService.addParentCategory(name, idChild, extractIdUserFromHeader(request));
    }

    @PostMapping("/add-item")
    public Item addItem(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                        @RequestPart(value = "data") Object obj,
                        @Autowired HttpServletRequest request) {

        // Validation des images liées au seul et unique subitem obligatoire lors de l'ajout d'un item
        if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
            MultipartFileListValidation.validMultipartFileList(files, null);
        }

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data = (JSONObject) JSONObject.wrap(obj);

        // Appel du service item pour la validation des données puis l'ajout en bdd
        return ItemService.addItem(data.optString("name"),
                data.optString("description"),
                data.optLong("idCategory"),
                data.optString("reference"),
                extractIdUserFromHeader(request),
                files);
    }

    @PostMapping("/add-subitem")
    public SubItem addSubItem(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                              @RequestPart(value = "data") Object obj) {

        // Validation des images liées au seul et unique subitem obligatoire lors de l'ajout d'un subitem
        if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
            MultipartFileListValidation.validMultipartFileList(files, null);
        }

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data = (JSONObject) JSONObject.wrap(obj);

        // Appel du service subitem pour la validation des données puis l'ajout en bdd
        return SubItemService.addSubItem(data.optString("reference"),
                data.optLong("idItem"),
                files);
    }

    @PostMapping("/add-tracking-sheet")
    public SubItem addTrackingSheet(@RequestBody HashMap<String, String> hashMap) {
        String comment = hashMap.get("comment");
        Long idSubItem = hashMap.get("idSubItem") != null ? Long.parseLong(hashMap.get("idSubItem")) : null;
        if (idSubItem == null) throw new CustomBadRequestException("Le paramètre idSubItem ne peut pas être null");
        TrackingSheetService.addTrackingSheet(comment, idSubItem);
        return SubItemService.getSubItemById(idSubItem);
    }


    //************************************************** PATCH
    @PatchMapping("/rename-category")
    public void renameCategory(@RequestBody HashMap<String, String> hashMap) {
        String name = hashMap.get("name");
        Long idCategory = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        if (idCategory == null) throw new CustomBadRequestException("Le paramètre id ne peut pas être null");
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        CategoryService.renameCategory(idCategory, name);
    }

    @PatchMapping("/move-category")
    public void moveCategory(@RequestBody HashMap<String, String> hashMap) {
        Long idCategory = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        Long idCategoryDest = hashMap.get("idParent") != null ? Long.parseLong(hashMap.get("idParent")) : null;
        if (idCategory == null) throw new CustomBadRequestException("Le paramètre id ne peut pas être null");
        CategoryService.moveCategory(idCategory, idCategoryDest);
    }

    @PatchMapping("/rename-item")
    public void renameItem(@RequestBody HashMap<String, String> hashMap) {
        String name = hashMap.get("name");
        Long idItem = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        if (idItem == null) throw new CustomBadRequestException("Le paramètre id ne peut pas être null");
        if (name == null) throw new CustomBadRequestException("Le paramètre name ne peut pas être null");
        ItemService.renameItem(idItem, name);
    }

    @PatchMapping("/move-item")
    public void moveItem(@RequestBody HashMap<String, String> hashMap) {
        Long idItem = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        Long idCategoryDest = hashMap.get("idCategory") != null ? Long.parseLong(hashMap.get("idCategory")) : null;
        if (idItem == null) throw new CustomBadRequestException("Le paramètre idItem ne peut pas être null");
        ItemService.moveItem(idItem, idCategoryDest);
    }

    @PatchMapping("/rename-subitem")
    public void renameSubItem(@RequestBody HashMap<String, String> hashMap) {
        Long idSubItem = hashMap.get("id") != null ? Long.parseLong(hashMap.get("id")) : null;
        String reference = hashMap.get("reference");
        if (idSubItem == null) throw new CustomBadRequestException("Le paramètre idSubItem ne peut pas être null");
        if (reference == null) throw new CustomBadRequestException("Le paramètre reference ne peut pas être null");
        SubItemService.renameSubItem(idSubItem, reference);
    }

    @PatchMapping("/edit-subitem")
    public SubItem editSubItem(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                @RequestPart(value = "data") Object obj,
                                @Autowired HttpServletRequest request) {

        // Conversion en JSON de l'objet JSON reçu
        JSONObject data = (JSONObject) JSONObject.wrap(obj);

        // Récuprération du subitem en bdd
        SubItem subItem = SubItemService.getSubItemById(data.optLong("idSubItem"));

        // Check manuellement si c'est bien le proprietaire du subitem qui est à l'origine de la requête (bug validateur)
        if (subItem.getItem().getIdUser() != extractIdUserFromHeader(request)) {
            throw new CustomBadRequestException("L'id renseigné dans le header ne correspond pas à l'id du propriétaire de l'objet.");
        }

        // Extraction des images à supprimer
        List<Long> filesToDel = new ArrayList<>();
        JSONArray filesToDelJSONArray = (data.optJSONArray("filesToDel"));
        if (filesToDelJSONArray != null) {
            for (int i = 0; i < filesToDelJSONArray.length(); i++) {
                long id = filesToDelJSONArray.optLong(i);
                if (id != 0) filesToDel.add(id);
            }
        }

       // Validation des images
        if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
           
            List<Picture> pictures = new ArrayList<>();
            for (Picture picture : subItem.getUrlImages()) {
                if (!filesToDel.contains(picture.getId())) pictures.add(picture);
            }
            MultipartFileListValidation.validMultipartFileList(files, pictures);
        }

        // Appel du service subitem pour la validation des données puis l'ajout en bdd
        return SubItemService.editSubItem(data.optString("reference"), subItem, filesToDel, files);
    }

    //************************************************** DELETE
    @DeleteMapping("/delete-category")
    public void deleteCategory(@RequestParam long id) {
        CategoryService.deleteCategory(id);
    }

    @DeleteMapping("/delete-item")
    public void deleteItem(@RequestParam long id) {
        ItemService.deleteItem(id);
    }

    @DeleteMapping("/delete-subitem")
    public void deleteSubItem(@RequestParam long id, @Autowired HttpServletRequest request) {
        SubItem subItem = SubItemService.getSubItemById(id);
        // Check manuellement si c'est bien le proprietaire du subitem qui est à l'origine de la requête (bug validateur)
        if (subItem.getItem().getIdUser() != extractIdUserFromHeader(request)) {
            throw new CustomBadRequestException("L'id renseigné dans le header ne correspond pas à l'id du propriétaire de l'objet.");
        }
        SubItemService.deleteSubItem(subItem);
    }

    @DeleteMapping("/delete-tracking-sheets")
    public SubItem deleteTrackingSheets(@RequestParam List<String> ids, @Autowired HttpServletRequest request) {
        return TrackingSheetService.deleteTrackingSheetsById(ids, extractIdUserFromHeader(request));
    }


    //************************************************** SHARE
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handle (NumberFormatException e) {
        return ResponseEntity.badRequest().body(new CustomBadRequestException(e));
    }

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
