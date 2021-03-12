<template>
  <div class="container" id="dashboard">
    <div class="row">
      <div class="col-md-6">
        <div id="chart">
          <apexchart type="line" height="350" :options="chartOptions" :series="series"></apexchart>
        </div>
      </div>
      <div class="col-md-6">
        {{ info }}
      </div>
    </div>
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
    axios.get("http://localhost:8080/api/reporting/latest-suites")
    .then(response => (this.info = response))
  },
  data: function() {
    return {
      info: null,
      series: [{
        name: "Desktops",
        data: [10, 41, 35, 51, 49, 62, 69, 91, 148]
      }],
      chartOptions: {
        chart: {
          height: 350,
          type: 'line',
          zoom: {
            enabled: false
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'straight'
        },
        title: {
          text: 'Product Trends by Month',
          align: 'left'
        },
        grid: {
          row: {
            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
            opacity: 0.5
          },
        },
        xaxis: {
          categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep'],
        }
      },
    }
  },
};

</script>

<style scoped>
</style>
