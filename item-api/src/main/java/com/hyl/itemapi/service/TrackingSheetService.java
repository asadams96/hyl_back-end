package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.TrackingSheetDao;
import com.hyl.itemapi.model.TrackingSheet;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    public static void addTrackingSheet(String comment, Long idSubItem) {
        TrackingSheet trackingSheet = new TrackingSheet();
        trackingSheet.setDate(new Date());
        trackingSheet.setComment(comment);
        trackingSheet.setSubItem(SubItemService.getSubItemById(idSubItem));
        CustomValidator.validate(trackingSheet, TrackingSheet.AddValidation.class);
        trackingSheetDao.save(trackingSheet);
    }

    public static void deleteTrackingSheet(TrackingSheet trackingSheet) {
        trackingSheetDao.delete(trackingSheet);
    }
}
