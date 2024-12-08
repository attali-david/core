package com.example.app.core.space.repository;

import com.example.app.core.space.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceRepositoryCustom {
}
