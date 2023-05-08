package com.vnu.server.repository;
import com.vnu.server.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query(value = "select price from staff where id >= 4 order by id", nativeQuery = true)
    List<Integer> getStaffSingleAndConsumption();
}
