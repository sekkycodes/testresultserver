<template>
  <div id="app container">
    <div class="row header">
      <div class="col-md">
        <img src="./assets/trs_logo.svg">
      </div>
    </div>
    <div class="row content">
      <div class="col-md">
        <Dashboard />
      </div>
    </div>
  </div>
</template>

<script>
import Dashboard from './components/Dashboard.vue'

export default {
  name: 'App',
  components: {
    Dashboard
  }
}
</script>

