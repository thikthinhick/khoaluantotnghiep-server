package com.vnu.server.repository;

import com.vnu.server.entity.Staff;
import com.vnu.server.model.CSModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

}
