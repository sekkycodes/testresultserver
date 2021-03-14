<template>
  <div id="latest-result-pie-chart">
    <apexchart type="pie" width="380" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import VueApexCharts from "vue-apexcharts";
import axios from "axios";

export default {
  name: "LatestResultsPieChart",
  components: {
    apexchart: VueApexCharts,
  },
  mounted: function() {
    axios.get("http://localhost:8081/api/reporting/latest-suites")
        .then(response => {
          this.series = [0, 0, 0, 0]
          response.data.forEach(d => {
            this.series[0] += d.testCasesSkipped;
            this.series[1] += d.testCasesPassed;
            this.series[2] += d.testCasesFailed;
            this.series[3] += d.testCasesWithError;
          })
        })
  },
  data: function() {
    return {
      series: [0, 0, 0, 0],
      chartOptions: {
        chart: {
          width: 380,
          type: 'pie',
        },
        labels: ['Skipped', 'Passed', 'Failed', 'Error'],
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
