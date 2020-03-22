package com.hyl.batch.service;

import com.hyl.batch.dao.ItemDao;
import com.hyl.batch.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ItemService {

    private static ItemDao itemDao;

    @Autowired
    public ItemService(ItemDao itemDao) {
        ItemService.itemDao = itemDao;
    }

    public static List<Item> getAll() {
        return itemDao.findAll();
    }
}
