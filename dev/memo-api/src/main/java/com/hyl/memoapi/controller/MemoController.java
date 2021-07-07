package com.hyl.memoapi.controller;

import com.hyl.memoapi.model.Memo;
import com.hyl.memoapi.service.MemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/memos-test")
    public List<Memo> getMemosTest() {
        return memoService.doGetMemosTest();
    }
}
