<template>
  <div id="app">
    <header>
      <div class="navbar navbar-dark bg-dark shadow-sm">
        <div class="container d-flex justify-content-between">
          <!--<img src="./assets/trs_logo.svg" alt="logo"/> -->
          <div class="navbar-brand d-flex align-items-center">
            <strong>Test Result Server</strong>
          </div>
        </div>
      </div>
    </header>
    <main>
      <Dashboard />
    </main>
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

