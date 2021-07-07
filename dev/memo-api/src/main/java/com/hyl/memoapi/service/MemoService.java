package com.hyl.memoapi.service;

import com.hyl.memoapi.model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MemoService {

    //************************************************ LOGGER
    Logger logger = LoggerFactory.getLogger(MemoService.class);


    //************************************************ METHODES
    public List<Memo> doGetMemosTest() {
        Memo memo1 = new Memo(1L, new Date(), new Date(), "Sortir les poubelles",
                "Tous les mardi et jeudi soir, il faut sortir les poubelles");
        Memo memo2 = new Memo(2L, new Date(), new Date(), "Rendez-vous chez le médecin",
                "Le docteur Dupont doit me faire un fond de l'oeil, ramener le colyre");
        Memo memo3 = new Memo(3L, new Date(), new Date(), "Rapporter Ruy Blas",
                "Ramener Ruy Blas à la bibliothèque Saint-Hilaire");
        Memo memo4 = new Memo(4L, new Date(), new Date(), "Rembourser Jean", "Rendre les 50€ à Jean");

        List<Memo> memos = new ArrayList<>();
        memos.add(memo1);
        memos.add(memo2);
        memos.add(memo3);
        memos.add(memo4);

        return memos;
    }

}
