<template>
  <div class="container" id="dashboard">
    <div class="row">
      <div class="col-md-6">
        <LatestResultPieChart />
      </div>
      <div class="col-md-6">
        TBD: add another chart
      </div>
    </div>
  </div>
</template>

<script>
import LatestResultPieChart from "@/components/LatestResultPieChart";

export default {
  components: {
    LatestResultPieChart
  }
}

</script>

<style scoped>
</style>
