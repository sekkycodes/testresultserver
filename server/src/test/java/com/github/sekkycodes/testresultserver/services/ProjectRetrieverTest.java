package com.github.sekkycodes.testresultserver.services;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.testutils.FixtureHelper;
import com.github.sekkycodes.testresultserver.vo.ProjectVO;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectRetrieverTest {

  @Mock
  private TestSuiteExecutionRepository testSuiteExecutionRepository;

  private ProjectRetriever sut;

  @BeforeEach
  void beforeEach() {
    sut = new ProjectRetriever(testSuiteExecutionRepository);
  }

  @Nested
  class RetrieveAll {

    private TestSuiteExecution dummySuiteExecution;

    @BeforeEach
    void beforeEach() {
      dummySuiteExecution = FixtureHelper.buildTestSuiteExecution();

      when(testSuiteExecutionRepository.findAllProjects())
          .thenReturn(Collections.singletonList(dummySuiteExecution));
    }

    @Test
    void retrievesAllProjects() {
      Set<ProjectVO> projects = sut.retrieveAll();

      assertThat(projects.size()).isEqualTo(1);
      ProjectVO project = projects.iterator().next();
      assertThat(project.getName()).isEqualTo(dummySuiteExecution.getProject());
      assertThat(project.getTestType()).isNotEmpty();
      assertThat(project.getTestType().iterator().next())
          .contains(dummySuiteExecution.getTestType());
      assertThat(project.getEnvironments()).isNotEmpty();
      assertThat(project.getEnvironments().iterator().next())
          .contains(dummySuiteExecution.getEnvironment());
      assertThat(project.getLabels()).isNotEmpty();
      assertThat(project.getLabels().iterator().next())
          .contains(dummySuiteExecution.getLabels().iterator().next());
    }
  }
}
