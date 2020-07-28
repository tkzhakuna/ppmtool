package io.agileintelligence.ppmtool.web;


import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.domain.UserDTO;
import io.agileintelligence.ppmtool.services.MapValidationErrorService;
import io.agileintelligence.ppmtool.services.ProjectService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
   
    @Autowired
    private MapValidationErrorService mapValidationErrorService;




    @PostMapping("/{ownerId}")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project,@PathVariable Long ownerId, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName(),ownerId);
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
    	User user=new User();
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        user.setId(project.getOwner().getId());
        user.setFullName(project.getOwner().getFullName());
        project.setOwner(user);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }


    @GetMapping("/all")
    public List<Project> getAllProjects(Principal principal){
    	
    	List<Project>projects=(List<Project>)projectService.findAllProjects(principal.getName());
    	for(Project proj:projects) {
    		User user=new User();
    		user.setId(proj.getOwner().getId());
            user.setFullName(proj.getOwner().getFullName());
            proj.setOwner(user);
    	}
    	return projects;
    	}


    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<String>("Project with ID: '"+projectId+"' was deleted", HttpStatus.OK);
    }
    
    
}
