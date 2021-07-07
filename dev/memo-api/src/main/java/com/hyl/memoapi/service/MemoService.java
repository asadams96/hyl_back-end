package com.hyl.memoapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemoService {

    //************************************************ LOGGER
    Logger logger = LoggerFactory.getLogger(MemoService.class);

}
