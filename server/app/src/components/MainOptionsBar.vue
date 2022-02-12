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
             @click="setProject(project)">{{ project.name }}</a>
        </div>
      </div>

      <div class="col">
        <JUnitXmlUpload class="float-right"/>
      </div>
    </div>
  </div>
</template>

<script>
import JUnitXmlUpload from "@/components/JUnitXmlUpload";

export default {
  components: {
    JUnitXmlUpload
  },
  computed: {
    displayedProject: function () {
      const proj = this.$store.state.selection.project;
      return proj ? proj.name : "Project";
    },
    projects: function() {
      return this.$store.state.projects;
    },
  },
  methods: {
    setProject: function(project) {
      this.$store.commit("setProject", project);
    },
  },
}
</script>

<style>
.options-bar {
  margin: 2em;
}
</style>
