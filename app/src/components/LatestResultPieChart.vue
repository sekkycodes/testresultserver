<template>
  <div id="latest-result-pie-chart">
    <apexchart type="pie" width="380" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import VueApexCharts from "vue-apexcharts";
import axios from "axios";

export default {
  name: "Dashboard",
  components: {
    apexchart: VueApexCharts,
  },
  mounted: function() {
    axios.get("http://localhost:8081/api/reporting/latest-suites")
        .then(response => {
          this.info = response;

          this.series = [0, 0, 0, 0]
          response.data.forEach(d => {
            this.series[0] += d.testCasesPassed;
            this.series[1] += d.testCasesSkipped;
            this.series[2] += d.testCasesFailed;
            this.series[3] += d.testCasesWithError;
          })
        })
  },
  data: function() {
    return {
      info: null,
      series: [0, 0, 0, 0],
      chartOptions: {
        chart: {
          width: 380,
          type: 'pie',
        },
        labels: ['Passed', 'Skipped', 'Failed', 'Error'],
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
