package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.domain.TestSuiteExecution;
import com.github.sekkycodes.testresultserver.repositories.TestSuiteExecutionRepository;
import com.github.sekkycodes.testresultserver.vo.ProjectVO;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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

    Map<String, Set<Pair<String, String>>> mapped = testSuites.stream()
        .collect(Collectors.groupingBy(TestSuiteExecution::getProject,
            Collectors
                .mapping(e -> Pair.of(e.getEnvironment(), e.getTestType()), Collectors.toSet())));

    Set<ProjectVO> projects = new HashSet<>();
    for (Map.Entry<String, Set<Pair<String, String>>> entry : mapped.entrySet()) {
      ProjectVO project = ProjectVO.builder()
          .name(entry.getKey())
          .environments(entry.getValue().stream().map(Pair::getFirst).collect(Collectors.toSet()))
          .testType(entry.getValue().stream().map(Pair::getSecond).collect(Collectors.toSet()))
          .build();

      projects.add(project);
    }

    return projects;
  }
}
