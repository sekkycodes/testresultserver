<template>
  <div>
    <h2> {{ headline }} </h2>
    <apexchart id="trend-chart" ref="trendChart" type="bar" height="350" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import VueApexCharts from "vue-apexcharts";
import * as moment from "moment";

export default {
  name: "TrendChart",
  components: {
    apexchart: VueApexCharts,
  },
  methods: {
    buildLast14DaysArray: function () {
      return [...new Array(14)]
          .map((i, idx) =>
              moment()
                  .startOf("day")
                  .subtract(idx, "days")
                  .format("YYYY-MM-DD"));
    }
  },
  mounted() {
    let last14daysArray = this.buildLast14DaysArray();
    let resultMap = new Map(last14daysArray.map(d => [d, {
      passed: 0,
      skipped: 0,
      failed: 0,
      erroneous: 0
    }]));
    this.entries.forEach(e => {
      let executionDate = e.aggregatedByValues[1];
      let sumEntry = resultMap.get(executionDate);
      sumEntry.erroneous += e.testCasesWithError;
      sumEntry.failed += e.testCasesFailed;
      sumEntry.passed += e.testCasesPassed;
      sumEntry.skipped += e.testCasesSkipped;
    });

    let values = Array.from(resultMap.values());
    this.series = [
      { name: "Skipped", data: values.map(v => v.skipped) },
      { name: "Passed", data: values.map(v => v.passed) },
      { name: "Failed", data: values.map(v => v.failed) },
      { name: "Error", data: values.map(v => v.erroneous) },
    ];
  },
  data: function () {
    return {
      series: [{
        name: "Skipped",
        data: []
      }, {
        name: "Passed",
        data: []
      }, {
        name: "Failed",
        data: []
      }, {
        name: "Error",
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'bar',
          height: 350,
          stacked: true,
          stackType: '100%'
        },
        responsive: [{
          breakpoint: 480,
          options: {
            legend: {
              position: 'bottom',
              offsetX: -10,
              offsetY: 0
            }
          }
        }],
        xaxis: {
          categories: this.buildLast14DaysArray()
        },
        fill: {
          opacity: 1
        },
        legend: {
          position: 'right',
          offsetX: 0,
          offsetY: 50
        },
      }
    }
  },
  props: {
    headline: String,
    entries: Array
  }
}
</script>
