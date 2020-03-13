package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.SubItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
