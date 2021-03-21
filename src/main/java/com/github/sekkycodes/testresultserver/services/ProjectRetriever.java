package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.ProjectVO;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectRetriever {

  private final TestSuiteExecutionRepository testSuiteExecutionRepository;

  @Autowired
  public ProjectRetriever(
      TestSuiteExecutionRepository testSuiteExecutionRepository) {
    this.testSuiteExecutionRepository = Objects.requireNonNull(testSuiteExecutionRepository);
  }

  public Set<ProjectVO> retrieveAll() {
    Collection<TestSuiteExecution> testSuites = testSuiteExecutionRepository.findAllProjects();

    Map<String, ProjectDetails> mapped = new HashMap<>();
    for (TestSuiteExecution tse : testSuites) {
      ProjectDetails details = mapped.get(tse.getProject());
      if (details == null) {
        details = new ProjectDetails();
        mapped.put(tse.getProject(), details);
      }

      details.environments.add(tse.getEnvironment());
      details.testTypes.add(tse.getTestType());
      details.labels.addAll(tse.getLabels());
    }

    return mapped.entrySet().stream()
        .map(e ->
            ProjectVO.builder()
                .name(e.getKey())
                .testType(e.getValue().getTestTypes())
                .environments(e.getValue().getEnvironments())
                .labels(e.getValue().getLabels())
                .build())
        .collect(Collectors.toSet());
  }

  @Data
  private static class ProjectDetails {

    Set<String> labels = new HashSet<>();
    Set<String> testTypes = new HashSet<>();
    Set<String> environments = new HashSet<>();
  }
}
