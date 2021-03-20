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
  props: ['selection'],
  data: function() {
    return {
      testTypeMap: new Map()
    }
  },
  computed: {
    projectName: function() {
      return this.selection.project ? this.selection.project.name : null;
    }
  },
  watch: {
    selection: {
      immediate: true,
      deep: true,
      handler() {
        this.loadAggregationData();
      }
    }
  },
  methods: {
    loadAggregationData: function() {
      if(!this.selection.project || !this.selection.project.name) {
        return;
      }

      this.testTypeMap = new Map();

      console.log(">>>>> projectName: " + this.projectName);
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
