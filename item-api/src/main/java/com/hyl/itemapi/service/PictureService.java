package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.PictureDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PictureService {


    //************************************************** DAO
    private PictureDao pictureDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public PictureService(PictureDao pictureDao) {
        this.pictureDao = pictureDao;
    }
}
