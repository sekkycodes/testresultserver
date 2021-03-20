package com.github.sekkycodes.testresultserver.controllers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.services.ProjectRetriever;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.ProjectVO;
import java.util.Collection;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

  @Mock
  private ProjectRetriever projectRetriever;

  private ProjectController sut;

  @Nested
  class GetAllProjects {

    private ProjectVO dummyProject;

    @BeforeEach
    void beforeEach() {
      dummyProject = FixtureHelper.buildProject();

      when(projectRetriever.retrieveAll())
          .thenReturn(Collections.singleton(dummyProject));

      sut = new ProjectController(projectRetriever);
    }

    @Test
    void returnsProjects() {
      ResponseEntity<Collection<ProjectVO>> response = sut.getAllProjects();

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().size()).isEqualTo(1);
      assertThat(response.getBody().iterator().next()).isEqualTo(dummyProject);
    }
  }
}
