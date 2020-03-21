package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.TrackingSheetDao;
import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.TrackingSheet;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrackingSheetService {


    //************************************************** DAO
    private static TrackingSheetDao trackingSheetDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public TrackingSheetService(TrackingSheetDao trackingSheetDao) {
        TrackingSheetService.trackingSheetDao = trackingSheetDao;
    }


    //************************************************** METHODE
    public static String getCommentByIdLoan(Long idLoan) {
        if (idLoan == null) return  null;
        Optional<TrackingSheet> optSheet=  trackingSheetDao.findByIdLoan(idLoan);
        return optSheet.map(TrackingSheet::getComment).orElse(null);
    }

    public static void addTrackingSheet(String comment, Long idSubItem, String reference, Long idLoan) {
        TrackingSheet trackingSheet = new TrackingSheet();
        trackingSheet.setDate(new Date());
        trackingSheet.setComment(comment);
        trackingSheet.setIdLoan(idLoan);
        trackingSheet.setSubItem(
                idSubItem != null ? SubItemService.getSubItemById(idSubItem) : SubItemService.getSubItemByRef(reference));
        CustomValidator.validate(trackingSheet, TrackingSheet.AddValidation.class);
        trackingSheetDao.save(trackingSheet);
    }

    public static SubItem deleteTrackingSheetsById(List<String> ids, long idUser) {
        List<TrackingSheet> trackingSheets = new ArrayList<>();
        long idSubitem = 0;
        boolean firstIteration = true;
        for (String idStr : ids) {

            long id = Long.parseLong(idStr);
            TrackingSheet trackingSheet = TrackingSheetService.getTrackingSheetById(id);

            if (trackingSheet.getSubItem().getItem().getIdUser() != idUser) {
                throw new CustomBadRequestException("L'id renseigné dans le header ne correspond pas à l'id du propriétaire de l'objet.");
            }

            if (firstIteration) {
                idSubitem = trackingSheet.getSubItem().getId();
                firstIteration = false;
            } else if (idSubitem != trackingSheet.getSubItem().getId()) {
                throw new CustomBadRequestException("Les fiches de suivi n'appartiennent pas au même objet subitem");
            }

            trackingSheets.add(trackingSheet);
        }
        trackingSheets.forEach(TrackingSheetService::deleteTrackingSheet);
        return SubItemService.getSubItemById(idSubitem);
    }

    public static void deleteTrackingSheet(TrackingSheet trackingSheet) {
        trackingSheetDao.delete(trackingSheet);
    }

    public static TrackingSheet getTrackingSheetById(long id) {
        Optional<TrackingSheet> optSheet = trackingSheetDao.findById(id);
        if ( optSheet.isPresent() ) return optSheet.get();
        else throw new CustomNotFoundException("L'objet TrackingSheet avec pour id="+id+" n'a pas été trouvé.");
    }
}
