<template>
  <div id="trend-charts-container">
    <TrendFilterOptions />
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
import TrendFilterOptions from "@/components/TrendFilterOptions";

export default {
  name: "TrendCharts",
  components: {
    TrendFilterOptions,
    TrendChart
  },
  data: function() {
    return {
      testTypeMap: new Map()
    }
  },
  computed: {
    projectName: function() {
      return this.selection.project ? this.selection.project.name : null;
    },
    selection: function() {
      return this.$store.state.selection;
    },
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

      const filter = {
        "daysBack": 14,
        "projectName": this.projectName
      };

      if(this.selection.environment) {
        filter["environment"] = this.selection.environment;
      }

      if(this.selection.labels && this.selection.labels.length > 0) {
        filter["labels"] = this.selection.labels;
      }

      axios.post("http://backend:8081/api/reporting/aggregated", {
        "aggregations": [
          "TEST_TYPE",
          "DATE"
        ],
        "filter": filter
      }).then(response => {
        this.fillCharts(response.data);
      })
    },
    fillCharts: function(data) {
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
