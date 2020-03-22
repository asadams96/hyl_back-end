package com.hyl.batch.dao;

import com.hyl.batch.model.SubItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubItemDao extends JpaRepository<SubItem, Long> {
}
