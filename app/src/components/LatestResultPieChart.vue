<template>
  <div id="latest-result-pie-chart">
    <apexchart type="pie" width="380" :options="chartOptions" :series="series" :key="generateKey()"
               v-if="projectName"></apexchart>
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
  props: ['projectName'],
  watch: {
    projectName: {
      immediate: true,
      handler() {
        this.loadData();
      }
    }
  },
  methods: {
    loadData: function() {
      if(!this.projectName) {
        return
      }

      axios.get("http://localhost:8081/api/reporting/latest-suites?project=" + this.projectName)
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
    // this method is a trick to keep vue rendering the data in the chart correctly
    // see https://michaelnthiessen.com/force-re-render/ and https://github.com/apexcharts/vue-apexcharts/issues/185
    generateKey: function () {
      return JSON.stringify(this.series ?? "");
    }
  },
  data: function () {
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
