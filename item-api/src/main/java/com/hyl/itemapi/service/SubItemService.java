package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.SubItemDao;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SubItemService {


    //************************************************** DAO
    private SubItemDao subItemDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public SubItemService(SubItemDao subItemDao) {
        this.subItemDao = subItemDao;
    }


    //************************************************** METHODES
    protected static SubItem buildSubItem(Item item, String reference, List<MultipartFile> files) {

        // Construction du subitem
        SubItem subItem = new SubItem();
        subItem.setReference(reference);
        subItem.setItem(item);
        subItem.setUrlImages(new ArrayList<>());

        // Construction des 'Picture' (stockage des informations des images)
        files.forEach(multipartFile -> subItem.getUrlImages().add(PictureService.buildPicture(subItem, multipartFile)));

        // Retour
        return subItem;
    }

    /**
     * Validation du nouvel subitem et de ses composants avant l'ajout en bdd
     * @param subItem Le nouvel subitem
     */
    protected static void validAddSubItem(SubItem subItem) {
        CustomValidator.validate(subItem, SubItem.AddValidation.class);
        if(subItem.getUrlImages() != null) {
            subItem.getUrlImages().forEach(PictureService::validAddPicture);
        }
    }

    public boolean checkAtomicRef(String reference) {
        return subItemDao.findByReference(reference).isEmpty();
    }
}
