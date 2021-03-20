<template>
  <div class="container options-bar">
    <div class="form-row">
      <div class="col">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="projectDropDown" data-toggle="dropdown"
                aria-expanded="false">
          {{ displayedProject }}
        </button>
        <div class="dropdown-menu" aria-labelledby="projectDropDown">
          <a v-for="project in projects" :key="project.name" class="dropdown-item" href="#"
             @click="selection.project = project">{{ project.name }}</a>
        </div>
      </div>

      <div class="col">
        <JUnitXmlUpload/>
      </div>
    </div>
  </div>
</template>

<script>
import JUnitXmlUpload from "@/components/JUnitXmlUpload";
import axios from "axios";

export default {
  props: {
    selection: Object
  },
  components: {
    JUnitXmlUpload
  },
  created: function () {
    axios.get("http://localhost:8081/api/project/all")
        .then(response => {
          console.dir(response);
          this.projects = response.data;
        });
  },
  data: function () {
    return {
      projects: []
    }
  },
  computed: {
    displayedProject: function () {
      return this.selection.project ? this.selection.project.name : "Project";
    }
  }
}
</script>

<style>
.options-bar {
  margin: 2em;
}
</style>
