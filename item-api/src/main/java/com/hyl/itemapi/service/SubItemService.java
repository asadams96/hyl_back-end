package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.SubItemDao;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubItemService {


    //************************************************** DAO
    private static SubItemDao subItemDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public SubItemService(SubItemDao subItemDao) {
        SubItemService.subItemDao = subItemDao;
    }


    //************************************************** METHODES
    public SubItem addSubItem(String reference, long idItem, List<MultipartFile> files) {
        SubItem subItem = buildSubItem( ItemService.getItemById(idItem), reference, files );
        validAddSubItem(subItem);
        subItemDao.save(subItem);
        Hashtable<String, String> urlTable =  FileService.saveMultipartFile(subItem, files);
        PictureService.majUrlPicture(urlTable);
        return subItem;
    }

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

    public static boolean checkAtomicRef(String reference) {
        return subItemDao.findByReference(reference).isEmpty();
    }

    public static SubItem getSubItemById(long id) {
        Optional<SubItem> optSubItem = subItemDao.findById(id);
        if ( optSubItem.isPresent() ) return optSubItem.get();
        else throw new CustomNotFoundException("L'objet SubItem avec pour id="+id+" n'a pas été trouvé.");
    }
}
