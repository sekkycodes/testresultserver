<template>
  <div class="filter-bar">
    <div class="row justify-content-end">
      <div class="col-md">
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
      <div class="col-md">
        <b-form-group>
          <b-form-checkbox-group
              v-model="selected"
              @change="updateLabelSelection(selected)"
              :options="labels"
              name="buttons-1"
              buttons
          ></b-form-checkbox-group>
        </b-form-group>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  computed: {
    displayedEnvironment: function () {
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
    updateLabelSelection(selected) {
      this.$store.commit("setLabels", selected);
    },
  },
  data: function() {
    return {
      options: [1, 2, 3, 4],
      selected: []
    }
  }
}
</script>

<style>
.filter-bar {
  margin-bottom: 2em;
}
</style>
