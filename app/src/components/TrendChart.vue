<template>
  <div>
    <h2> {{ headline }} </h2>
    <apexchart id="trend-chart" ref="trendChart" type="bar" height="350" :options="trendChartOptions"
               :series="trendChartSeries"></apexchart>
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
    },
    updateDetailList: function (sourceChart, selectedDataPointIndex, seriesIndex) {
      console.log("selected test result type: " + this.trendSeries[seriesIndex].name);
      const label = sourceChart.axes.w.config.xaxis.categories[selectedDataPointIndex];
      console.log("selected date: " + label)
    },
    clearDetailList: function() {
      console.log("clear");
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
    this.trendSeries = [
      {name: "Skipped", data: values.map(v => v.skipped)},
      {name: "Passed", data: values.map(v => v.passed)},
      {name: "Failed", data: values.map(v => v.failed)},
      {name: "Error", data: values.map(v => v.erroneous)},
    ];
  },
  computed: {
    trendChartOptions: function () {
      return {
        chart: {
          type: 'bar',
          height: 350,
          stacked: true,
          stackType: '100%',
          events: {
            dataPointSelection: (e, chart, opts) => {

              // opts is an array of arrays, where only the last element is set (for single item selection)
              // if the last item of the array has a value, then that value was selected in the chart
              const selectedDataPointIndex = opts.selectedDataPoints[opts.selectedDataPoints.length-1];
              if (selectedDataPointIndex.length === 1) {
                this.updateDetailList(chart, selectedDataPointIndex[0], opts.seriesIndex);
              } else {
                this.clearDetailList();
              }
            }
          },
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
        }
      }
    },
    trendChartSeries: function () {
      return this.trendSeries
    }
  },
  data: function () {
    return {
      trendSeries: [{
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
      }
      ]
    }
  },
  props: {
    headline: String,
    entries: Array
  }
}
</script>
