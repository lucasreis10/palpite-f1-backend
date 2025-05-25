package com.lucasreis.palpitef1backend.domain.pilot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PilotRepository extends JpaRepository<Pilot, Long> {
    
    Optional<Pilot> findByDriverId(String driverId);
    
    List<Pilot> findByNationalityIgnoreCase(String nationality);
    
    @Query("SELECT p FROM Pilot p WHERE " +
           "LOWER(p.givenName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.familyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pilot> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT p FROM Pilot p ORDER BY p.familyName, p.givenName")
    List<Pilot> findAllOrderByName();
    
    // Métodos que consideram apenas pilotos ativos
    @Query("SELECT p FROM Pilot p WHERE p.active = true ORDER BY p.familyName, p.givenName")
    List<Pilot> findAllActiveOrderByName();
    
    List<Pilot> findByActiveOrderByFamilyNameAscGivenNameAsc(Boolean active);
    
    List<Pilot> findByNationalityIgnoreCaseAndActive(String nationality, Boolean active);
    
    @Query("SELECT p FROM Pilot p WHERE p.active = :active AND (" +
           "LOWER(p.givenName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.familyName) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<Pilot> findByNameContainingAndActive(@Param("name") String name, @Param("active") Boolean active);
    
    boolean existsByDriverId(String driverId);
} 