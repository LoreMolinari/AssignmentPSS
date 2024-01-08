package com.spaceagency.repository;

import com.spaceagency.model.Astronaut;
import com.spaceagency.model.Spacesuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpacesuitRepository extends JpaRepository<Spacesuit, Long> {

    @Query(value = "SELECT * FROM spacesuit WHERE model = ?1", nativeQuery = true)
    List<Spacesuit> findByName(String name);

    @Query("SELECT s FROM Spacesuit s WHERE :astronaut = s.astronaut")
    Spacesuit findByAstronautId(@Param("astronaut") Astronaut astronaut);

}
