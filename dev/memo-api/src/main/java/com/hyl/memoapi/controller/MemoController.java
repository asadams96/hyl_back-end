package com.hyl.memoapi.controller;

import com.hyl.memoapi.exception.CustomBadRequestException;
import com.hyl.memoapi.model.Memo;
import com.hyl.memoapi.service.MemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "memo")
public class MemoController {

    //************************************************ LOGGER
    Logger logger = LoggerFactory.getLogger(MemoController.class);

    //************************************************ BEANS
    private final MemoService memoService;

    //************************************************ CONSTRUCTOR
    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }


    //************************************************ METHODES
    @GetMapping("/memos-by-id_user")
    public List<Memo> getMemosByIdUser(HttpServletRequest request) {
        return memoService.doGetMemosByIdUser(extractIdUserFromHeader(request));
    }

    private long extractIdUserFromHeader (HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }

        try {
            return Long.parseLong(idUserStr);
        } catch (NumberFormatException e) {
            throw new CustomBadRequestException("L'id de l'utilisateur doit être un nombre.");
        }
    }
}
