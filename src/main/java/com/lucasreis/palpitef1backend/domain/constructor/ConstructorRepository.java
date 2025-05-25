package com.lucasreis.palpitef1backend.domain.constructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConstructorRepository extends JpaRepository<Constructor, Long> {
    
    Optional<Constructor> findByConstructorId(String constructorId);
    
    List<Constructor> findByNationalityIgnoreCase(String nationality);
    
    @Query("SELECT c FROM Constructor c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Constructor> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM Constructor c ORDER BY c.name")
    List<Constructor> findAllOrderByName();
    
    // Métodos que consideram apenas escuderias ativas
    @Query("SELECT c FROM Constructor c WHERE c.active = true ORDER BY c.name")
    List<Constructor> findAllActiveOrderByName();
    
    List<Constructor> findByActiveOrderByNameAsc(Boolean active);
    
    List<Constructor> findByNationalityIgnoreCaseAndActive(String nationality, Boolean active);
    
    @Query("SELECT c FROM Constructor c WHERE c.active = :active AND " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Constructor> findByNameContainingAndActive(@Param("name") String name, @Param("active") Boolean active);
    
    boolean existsByConstructorId(String constructorId);
} 