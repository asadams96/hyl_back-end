package com.hyl.batch.service;

import com.hyl.batch.dao.SubItemDao;
import com.hyl.batch.model.SubItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class ItemService {

    private static SubItemDao subItemDao;

    @Autowired
    public ItemService(SubItemDao subItemDao) {
        ItemService.subItemDao = subItemDao;
    }


    protected static SubItem getSubItemByReference(String reference) {
        Optional<SubItem> optSubItem = subItemDao.findByReference(reference);
        if ( optSubItem.isPresent() ) return optSubItem.get();
        else throw new RuntimeException("L'objet SubItem avec pour reference="+reference+" n'a pas été trouvé.");
    }
}
