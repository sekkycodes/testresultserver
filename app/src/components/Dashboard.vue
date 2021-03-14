<template>
  <div class="container" id="dashboard">
    <div class="row">
      <div class="col-md-6">
        <LatestResultPieChart />
      </div>
      <div class="col-md-6">
        <TestTypePieChart />
      </div>
    </div>
  </div>
</template>

<script>
import LatestResultPieChart from "@/components/LatestResultPieChart";
import TestTypePieChart from "@/components/TestTypePieChart";

export default {
  components: {
    LatestResultPieChart,
    TestTypePieChart
  }
}

</script>

<style scoped>
</style>
