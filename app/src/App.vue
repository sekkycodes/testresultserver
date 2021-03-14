<template>
  <div id="app">
    <header>
      <div class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container d-flex justify-content-between">
          <div class="navbar-brand d-flex align-items-center">
            <img id="navbar-logo" src="./assets/trs_logo.svg" alt="logo"/>
            <strong>Test Result Server</strong>
          </div>
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item">
                <JUnitXmlUpload />
              </li>
            </ul>
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
import JUnitXmlUpload from "@/components/JUnitXmlUpload";

export default {
  name: 'App',
  components: {
    Dashboard,
    JUnitXmlUpload
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

