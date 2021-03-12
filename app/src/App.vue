<template>
  <div id="app">
    <header>
      <div class="navbar navbar-dark bg-dark shadow-sm">
        <div class="container d-flex justify-content-between">
          <div class="navbar-brand d-flex align-items-center">
            <img id="navbar-logo" src="./assets/trs_logo.svg" alt="logo"/>
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

<style>
#navbar-logo {
  height: 50px;
}
</style>

