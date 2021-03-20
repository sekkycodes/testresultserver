<template>
  <div class="container" id="dashboard">
    <div class="row">
      <div class="col-md-12">
        <h1>Latest Results</h1>
      </div>
      <div class="col-md-6">
        <LatestResultPieChart />
      </div>
      <div class="col-md-6">
        <TestTypePieChart />
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <h1>Trends</h1>
      </div>
      <div class="col-md-12">
        <TrendCharts />
      </div>
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
  }
}

</script>

<style scoped>
</style>
