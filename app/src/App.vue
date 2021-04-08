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
      <MainOptionsBar />
      <Dashboard />
    </main>
  </div>
</template>

<script>
import Dashboard from './components/Dashboard.vue'
import MainOptionsBar from './components/MainOptionsBar.vue'
import axios from "axios";

export default {
  name: 'App',
  components: {
    MainOptionsBar,
    Dashboard
  },
  data: function() {
    return {
      loadedProjects: [],
      selectionData: {
        project: null,
        environment: null,
      }
    }
  },
  created: function () {
    axios.get("http://backend:8081/api/project/all")
        .then(response => {
          this.$store.commit("updateProjects", response.data);
        });
  },
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

