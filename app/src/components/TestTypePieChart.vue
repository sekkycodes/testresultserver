<template>
  <div id="latest-result-pie-chart">
    <apexchart id="test-type-chart" ref="typeApexChart" type="pie" width="380" :options="chartOptions" :series="series"
               :key="generateKey()"></apexchart>
  </div>
</template>

<script>
import VueApexCharts from "vue-apexcharts";
import axios from "axios";

export default {
  name: "TestTypePieChart",
  components: {
    apexchart: VueApexCharts,
  },
  created: function () {
    axios.get("http://localhost:8081/api/reporting/latest-suites")
        .then(response => {

          this.series = []
          this.chartOptions.labels = []

          let testTypeMap = new Map();

          response.data.forEach(d => {
            let tcs = d.testCasesTotal;
            if (testTypeMap.has(d.testType)) {
              tcs += testTypeMap.get(d.testType);
            }
            testTypeMap.set(d.testType, tcs);
          })

          this.series = Array.from(testTypeMap.values());
          this.chartOptions.labels = Array.from(testTypeMap.keys());

          this.$refs.typeApexChart.refresh();
          this.$refs.typeApexChart.updateSeries(this.series);
        })
  },
  methods: {
    // this method is a trick to keep vue rendering the data in the chart correctly
    // see https://michaelnthiessen.com/force-re-render/ and https://github.com/apexcharts/vue-apexcharts/issues/185
    generateKey: function() {
      return JSON.stringify(this.series ?? "");
    }
  },
  data: function () {
    return {
      series: [0],
      chartOptions: {
        chart: {
          width: 380,
          type: 'pie',
        },
        labels: [''],
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
    }
  },
};

</script>
