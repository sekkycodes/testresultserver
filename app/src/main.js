import Vue from 'vue'
import Vuex from "vuex";
import App from './App.vue'

import "bootstrap";
import "bootswatch/dist/darkly/bootstrap.min.css";
import "moment";
import BootstrapVue from "bootstrap-vue";
import "bootstrap-vue/dist/bootstrap-vue.css"
import VueToastr from "vue-toastr";

Vue.config.productionTip = false
Vue.use(BootstrapVue);
Vue.use(Vuex);
Vue.use(VueToastr);

const store = new Vuex.Store({
  state: {
    projects: [],
    selection: {
      project: null,
      environment: null,
      testType: null,
      labels: []
    }
  },
  mutations: {
    updateProjects (state, payload) {
      state.projects = payload;
    },
    setProject (state, payload) {
      state.selection.project = payload;
    },
    setEnvironment (state, payload) {
      state.selection.environment = payload;
    },
    setTestType (state, payload) {
      state.selection.testType = payload;
    },
    setLabels (state, payload) {
      state.selection.labels = payload;
    },
    resetSelection (state) {
      state.selection = {
        project: null,
        environment: null,
        testType: null,
        labels: []
      }
    },
  }
});

new Vue({
  render: h => h(App),
  store,
}).$mount('#app')
