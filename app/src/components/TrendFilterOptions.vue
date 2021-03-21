<template>
  <div class="filter-bar">
    <div class="form-row">
      <div class="col">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="environmentDropDown" data-toggle="dropdown"
                aria-expanded="false">
          {{ displayedEnvironment }}
        </button>
        <div class="dropdown-menu" aria-labelledby="environmentDropDown">
          <a class="dropdown-item" @click="selectEnvironment(null)">All Environments</a>
          <a v-for="env in environments" :key="env" class="dropdown-item" @click="selectEnvironment(env)">
            {{ env }}
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: [],
  computed: {
    displayedEnvironment: function() {
      const env = this.$store.state.selection.environment;
      return env ?? "All Environments";
    },
    environments: function () {
      const proj = this.$store.state.selection.project;
      if (!proj) {
        return [];
      }

      return proj.environments;
    },
    labels: function () {
      const proj = this.$store.state.selection.project;
      if (!proj) {
        return [];
      }

      return proj.labels;
    },
  },
  methods: {
    selectEnvironment: function (env) {
      this.$store.commit("setEnvironment", env);
    },
    selectLabel: function (label) {
      this.$store.commit("addLabel", label);
    },
    deselectLabel: function (label) {
      this.$store.commit("removeLabel", label);
    }
  },
}
</script>

<style>
.filter-bar {
  margin-bottom: 2em;
}
</style>
