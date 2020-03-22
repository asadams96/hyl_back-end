package com.hyl.batch.controller;

import com.hyl.batch.model.Item;
import com.hyl.batch.model.Loan;
import com.hyl.batch.model.User;
import com.hyl.batch.service.ItemService;
import com.hyl.batch.service.LoanService;
import com.hyl.batch.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BatchController implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(BatchController.class);


    @Override
    public void run(String... args) throws Exception {

        // TODO -> Pour test la bdd et ses 3 schémas
        List<User> users = UserService.getAll();
        List<Loan> loans = LoanService.getAll();
        List<Item> items = ItemService.getAll();

        users.forEach(user -> logger.warn(user.toString()));
        loans.forEach(loan -> logger.warn(loan.toString()));
        items.forEach(item -> logger.info(item.toString()));
    }
}
