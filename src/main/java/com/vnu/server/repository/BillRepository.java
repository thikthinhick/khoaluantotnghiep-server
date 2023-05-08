package com.vnu.server.repository;

import com.vnu.server.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(value = "CALL sp_bill_current(@staff_type, @staff_type_change);", nativeQuery = true)
    String spBillCurrent();
    @Modifying
    @Transactional
    @Query(value = "CALL sp_bill_update(?1);", nativeQuery = true)
    void spBillUpdate(Integer bill_id);
    @Modifying
    @Transactional
    void deleteBillByTime(String time);
}
