package com.no.awards.repository;

import com.no.awards.model.Employee;
import com.no.awards.configuration.RedisConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Override
    @Transactional
    @CacheEvict(value = RedisConfiguration.CacheNames.EMPLOYEES, allEntries = true)
    <S extends Employee> S save(S entity);

    @Modifying
    @Transactional
    @CacheEvict(value = RedisConfiguration.CacheNames.EMPLOYEES, allEntries = true)
    @Query(
        nativeQuery = true,
        value = """
            delete from employee where id = :id
            """
    )
    void delete(String id);

    @Query(
        nativeQuery = true,
        value = """
            select * from employee
            """
    )
    Page<Employee> getEmployees(Pageable pageable);

    @Query(
        nativeQuery = true,
        value = """
                select coalesce(sum(awards), 0) from employee
            """
    )
    int getTotalAwards();
}
