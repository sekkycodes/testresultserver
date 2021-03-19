<template>
  <div id="trend-charts-container">
    <div class="row" v-for="typeEntries in Array.from(testTypeMap.entries())" :key="typeEntries.key">
      <div class="col-md-12">
        <TrendChart :test-type="typeEntries[0]" :entries="typeEntries[1]" :project="projectName" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TrendChart from "@/components/TrendChart";

export default {
  name: "TrendCharts",
  components: {
    TrendChart
  },
  data: function() {
    return {
      testTypeMap: new Map(),
      projectName: 'project01'
    }
  },
  methods: {
    loadAggregationData: function() {
      axios.post("http://localhost:8081/api/reporting/aggregated", {
        "aggregations": [
          "TEST_TYPE",
          "DATE"
        ],
        "filter": {
          "daysBack": 14,
          "projectName": this.projectName
        }
      }).then(response => {
        this.fillCharts(response.data);
      })
    },
    fillCharts: function(data) {
      console.log("TrendCharts - received data: ");
      console.dir(data);

      let resultMap = new Map();

      data.entries.forEach(entry => {
        let testType = entry.aggregatedByValues[0];
        if(!resultMap.has(testType)) {
          resultMap.set(testType, []);
        }
        resultMap.get(testType).push(entry);
      });

      this.testTypeMap = resultMap;

      console.log("TrendCharts - testTypeMap: ")
      console.dir(resultMap);
    }
  },
  mounted: function () {
    this.loadAggregationData();
  }
}
</script>

<style>
#trend-charts-container {
  margin-top: 1em;
  margin-bottom: 2em;
}
</style>
