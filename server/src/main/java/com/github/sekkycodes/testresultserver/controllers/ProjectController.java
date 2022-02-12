package com.github.sekkycodes.testresultserver.controllers;

import com.github.sekkycodes.testresultserver.services.ProjectRetriever;
import com.github.sekkycodes.testresultserver.vo.ProjectVO;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for retrieving project information
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

  private final ProjectRetriever projectRetriever;

  @Autowired
  public ProjectController(ProjectRetriever projectRetriever) {
    this.projectRetriever = Objects.requireNonNull(projectRetriever);
  }

  /**
   * Endpoint for retrieving all project information.
   *
   * @return a collection of all projects
   */
  @GetMapping("/all")
  public ResponseEntity<Collection<ProjectVO>> getAllProjects() {
    Set<ProjectVO> projects = projectRetriever.retrieveAll();

    return ResponseEntity.ok(projects);
  }
}
