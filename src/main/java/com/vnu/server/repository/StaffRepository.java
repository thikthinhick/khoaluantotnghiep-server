package com.vnu.server.repository;
import com.vnu.server.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query(value = "CALL sp_get_staff(@staff_type, @staff_type_change);", nativeQuery = true)
    String spGetStaff();
    @Modifying
    @Query(value = "update app_schedule set value = ?1 where id = 1;", nativeQuery = true)
    void updateStaffHome(Long id);
}
