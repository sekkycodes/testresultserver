<template>
  <div id="dashboard">
    <div class="container" v-if="selection.project">
      <div class="row">
        <div class="col-md-12">
          <h3>Latest Results</h3>
        </div>
        <div class="col-md-6">
          <LatestResultPieChart :project-name="projectName" />
        </div>
        <div class="col-md-6">
          <TestTypePieChart :project-name="projectName" />
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <h3>Trends</h3>
        </div>
        <div class="col-md-12">
          <TrendCharts :selection="selection"/>
        </div>
      </div>
    </div>
    <div class="container" v-else>
      <h4> Select Project to display results </h4>
    </div>
  </div>
</template>

<script>
import LatestResultPieChart from "@/components/LatestResultPieChart";
import TestTypePieChart from "@/components/TestTypePieChart";
import TrendCharts from "@/components/TrendCharts";

export default {
  components: {
    TrendCharts,
    LatestResultPieChart,
    TestTypePieChart
  },
  computed: {
    projectName: function() {
      return this.selection.project
          ? this.selection.project.name
          : null;
    }
  },
  props: {
    selection: Object
  }
}

</script>

<style scoped>
</style>
