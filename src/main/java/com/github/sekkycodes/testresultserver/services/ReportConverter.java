package com.github.sekkycodes.testresultserver.services;

import com.github.sekkycodes.testresultserver.vo.TestSuiteExecutionVO;
import com.github.sekkycodes.testresultserver.vo.reporting.AggregatedReport.AggregatedReportEntry;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.stereotype.Service;

/**
 * Converts canonical value objects to report value objects
 */
@Service
public class ReportConverter {

  /**
   * Converts a test suite execution value object to an aggregated report entry. This report entry
   * can then be used for further aggregations.
   *
   * @param suite the suite execution object to be converted
   * @return a converted report entry
   */
  public AggregatedReportEntry toAggregatedReportEntry(TestSuiteExecutionVO suite) {
    return AggregatedReportEntry.builder()
        .aggregatedByValues(new ArrayList<>())
        .duration(suite.getDuration())
        .testCasesTotal(suite.getTestCasesTotal())
        .testCasesPassed(suite.getTestCasesPassed())
        .testCasesSkipped(suite.getTestCasesSkipped())
        .testCasesWithError(suite.getTestCasesWithError())
        .testCasesFailed(suite.getTestCasesFailed())
        .testSuiteExecutionIds(Collections
            .singletonList(new SimpleEntry<>(suite.getIdName(), suite.getIdTime())))
        .build();
  }
}
