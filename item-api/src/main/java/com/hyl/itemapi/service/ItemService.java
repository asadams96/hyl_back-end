package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
