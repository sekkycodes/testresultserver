<template>
  <div>
    <b-table striped hover :items="testCases"></b-table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    date: String,
    result: String
  },
  watch: {
    date: function() {
      this.loadData();
    },
    result: function() {
      this.loadData()
    }
  },
  methods: {
    loadData: function() {
      axios.get("http://localhost:8081/api/testcase/by-date-and-result?date=" + this.date + "&result=" + this.result)
          .then(response => {
            this.testCases = response.data;
            this.testCases.forEach(tc => {
              tc.id = tc.idName + tc.idTime;
            })
          });
    }
  },
  mounted() {
    this.loadData();
  },
  data: function() {
    return {
      testCases: []
    }
  }
}
</script>
