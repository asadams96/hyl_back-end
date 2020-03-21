package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.SubItemDao;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.TrackingSheet;
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
    public static void renameSubItem(long idSubItem, String reference) {
        SubItem subItem = getSubItemById(idSubItem);
        if (!subItem.getReference().equals(reference)) {
            subItem.setReference(reference);
            CustomValidator.validate(subItem, SubItem.UpdateValidation.class);
            subItemDao.save(subItem);
        }
    }

    public static SubItem addSubItem(String reference, long idItem, List<MultipartFile> files) {
        SubItem subItem = buildSubItem( ItemService.getItemById(idItem), reference, files );
        validAddSubItem(subItem);
        subItemDao.save(subItem);
        if (files != null) {
            Hashtable<String, String> urlTable =  FileService.saveMultipartFile(subItem, files);
            PictureService.majUrlPicture(urlTable);
        }
        return subItem;
    }

    public static void deleteSubItem(SubItem subItem) {

        // Si c'est le dernier subitem de l'item -> true -> l'item sera aussi supprimé
        boolean deleteItem = subItem.getItem().getSubItems().size() == 1;

        // Suppresion du subitem de la liste des subitems de l'item (pour que la suppresion en bdd fonctionne -> cascade)
        subItem.getItem().getSubItems().remove(subItem);

        // Suppression des fiches de suivi du subitem
        List<TrackingSheet> trackingSheets = new ArrayList<>(subItem.getTrackingSheets());
        trackingSheets.forEach(trackingSheet -> {
            subItem.getTrackingSheets().remove(trackingSheet);
            TrackingSheetService.deleteTrackingSheet(trackingSheet);
        });

        // Suppresion du subitem
        subItemDao.delete(subItem);

        // Suppresion du répertoire contenant les images
        FileService.deleteFile(subItem);

        // Si c'est le dernier subitem de l'item -> true -> l'item sera aussi supprimé
        if (deleteItem) ItemService.deleteItem(subItem.getItem().getId());
    }

    public static SubItem editSubItem(String reference, SubItem subItem,
                                      List<Long> filesToDel, List<MultipartFile> files) {

        // Mise à jour de la référence si elle a changé
        if (!subItem.getReference().equals(reference)) {
            subItem.setReference(reference);
            CustomValidator.validate(subItem, SubItem.UpdateValidation.class);
        }

        // Supression des images à supprimer en bdd et sur la machine
        if (filesToDel != null) {
            for (long id : filesToDel) {
                PictureService.deletePictureById(subItem, id);
            }
        }

        // Construction des 'Picture' (stockage des informations des images)
        if (files != null) {
            for (MultipartFile file : files) {
                subItem.getUrlImages().add(PictureService.buildPicture(subItem, file));
            }
        }

        // Mise à jour du subitem en bdd avec ses nouvelles images
        subItem = subItemDao.save(subItem);

        if (files != null) {
            // Ajout des nouvelles images sur la machine
            Hashtable<String, String> urlTable = FileService.saveMultipartFile(subItem, files);

            // Mise à jour de l'url des 'Picture' en bdd
            PictureService.majUrlPicture(urlTable);
        }

        // Renvoi du subitem mis à jour
        return subItem;
    }

    protected static SubItem buildSubItem(Item item, String reference, List<MultipartFile> files) {

        // Construction du subitem
        SubItem subItem = new SubItem();
        subItem.setReference(reference);
        subItem.setItem(item);
        subItem.setUrlImages(new ArrayList<>());

        if (files != null) {
            // Construction des 'Picture' (stockage des informations des images)
            files.forEach(multipartFile -> subItem.getUrlImages().add(PictureService.buildPicture(subItem, multipartFile)));
        }

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

     public static SubItem getSubItemByRef(String reference) {
        Optional<SubItem> optSubItem = subItemDao.findByReference(reference);
        if ( optSubItem.isPresent() ) return optSubItem.get();
        else throw new CustomNotFoundException("L'objet SubItem avec pour reference="+reference+" n'a pas été trouvé.");
    }

}
