package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.ItemDao;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Service
@Transactional
public class ItemService {


    //************************************************** DAO
    private ItemDao itemDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    //************************************************** METHODES
    public Item addItem(String name, String description, long idCategory,
                        String reference, long idUser, List<MultipartFile> files) {

        // Construction de l'item
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setIdUser(idUser);
        item.setSubItems(new ArrayList<>());
        if (idCategory != 0) item.setCategory(CategoryService.getCategoryById(idCategory));

        // Construction du subitem
        item.getSubItems().add(SubItemService.buildSubItem(item, reference, files));

        // Validation du nouvel item et de ses composant
        validAddItem(item);

        // Enregistrement du nouvel item
        item =  itemDao.save(item);

        // Enregistrement des images et mise à jour en bdd
        item.getSubItems().forEach(subItem -> {
            Hashtable<String, String> urlTable =  FileService.saveMultipartFile(subItem, files);
            PictureService.majUrlPicture(urlTable);
        });

        // Retour
        return item;
    }

    /**
     * Validation du nouvel item et de ses composants avant l'ajout en bdd
     * @param item Le nouvel item
     */
    private void validAddItem(Item item) {
        CustomValidator.validate(item, Item.AddValidation.class);
        item.getSubItems().forEach(SubItemService::validAddSubItem);
    }

    public boolean checkAtomicName(String name) {
        return itemDao.findByName(name).isEmpty();
    }
}
