package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.TrackingSheetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrackingSheetService {


    //************************************************** DAO
    private TrackingSheetDao trackingSheetDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public TrackingSheetService(TrackingSheetDao trackingSheetDao) {
        this.trackingSheetDao = trackingSheetDao;
    }
}
