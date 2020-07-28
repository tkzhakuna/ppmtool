package io.agileintelligence.ppmtool.repositories;

import io.agileintelligence.ppmtool.domain.Project;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	@Query("SELECT p FROM Project p WHERE p.projectIdentifier = ?1")
    Project findByProjectIdentifier(String projectId);

    @Override
    Iterable<Project> findAll();
    
    @Query("SELECT p FROM Project p WHERE p.projectLeader = ?1 or p.owner.username = ?1")
    Iterable<Project> findAllByProjectLeader(String username);
    
    
   
}
