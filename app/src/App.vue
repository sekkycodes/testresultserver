<template>
  <div id="app">
    <header>
      <div class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container d-flex justify-content-between">
          <div class="navbar-brand d-flex align-items-center">
            <img id="navbar-logo" src="./assets/trs_logo.svg" alt="logo"/>
            <strong>Test Result Server</strong>
          </div>
        </div>
      </div>
    </header>
    <main>
      <OptionsBar :selection="selection" />
      <Dashboard :selection="selection" />
    </main>
  </div>
</template>

<script>
import Dashboard from './components/Dashboard.vue'
import OptionsBar from './components/OptionsBar.vue'

export default {
  name: 'App',
  components: {
    OptionsBar,
    Dashboard
  },
  data: function() {
    return {
      selection: {
        project: null,
        environment: null,
      }
    }
  }
}
</script>

<style>
#navbar-logo {
  height: 50px;
}

header {
  margin-bottom: 2em
}
</style>

