<template>
  <div>
    <b-table
        striped
        selectable
        select-mode="single"
        hover
        :items="testCases"
        :fields="fields"
        @row-selected="onRowSelected"
    ></b-table>

    <!-- Modal -->
    <b-modal
        v-model="showModal"
        :title="modalTitle"
        size="lg"
    >
      <div v-if="message">
        <h5>Message</h5>
        <p> {{ this.message }} </p>
      </div>
      <div class="stacktrace-container" v-if="details">
        <h5>Error Details</h5>
        <p class="text-monospace stacktrace" v-html="this.details">{{ this.details }}</p>
      </div>
    </b-modal>
  </div>
</template>

<script>
import axios from "axios";
import * as moment from "moment";

export default {
  props: {
    date: String,
    result: String,
    project: String,
    testType: String
  },
  watch: {
    date: function () {
      this.loadData();
    },
    result: function () {
      this.loadData()
    }
  },
  methods: {
    loadData: function () {
      const params = {
        date: this.date,
        result: this.result,
        project: this.project,
        testType: this.testType
      };

      axios.get("http://localhost:8081/api/testcase/by-date-and-result", { params })
          .then(response => {
            this.testCases = response.data;
            this.testCases.forEach(tc => {
              tc.id = tc.idName + tc.idTime;
            })

            console.dir(this.testCases);
          });
    },
    removeLineBreaks: function(str) {
      if(!str){
        return "";
      }
      return str.trim().replace(/(?:\r\n|\r|\n)/g, '<br>');
    },
    onRowSelected: function (item) {
      this.selectedItem = item[0];
      this.showModal = this.selectedItem && true;
    }
  },
  mounted() {
    this.loadData();
  },
  computed: {
    message: function() {
      if(!this.selectedItem) {
        return "";
      }
      return this.selectedItem.message ?? ""
    },
    details: function() {
      if(!this.selectedItem) {
        return "";
      }
      return this.removeLineBreaks(this.selectedItem.details ?? "");
    },
    modalTitle: function() {
      return this.selectedItem ? "Details for Test Case " + this.selectedItem.idName : "";
    }
  },
  data: function () {
    return {
      showModal: false,
      selectedItem: null,
      fields: [
        {
          key: 'idName',
          label: 'Test Case Name',
          sortable: true
        },
        {
          key: 'idTime',
          label: 'Executed At',
          sortable: true,
          formatter: (value) => {
            return moment(new Date(value)).format("YYYY-MM-DD HH:mm:ss");
          }
        },
        {
          key: 'duration',
          label: "Duration (in sec)",
          sortable: true,
          formatter: (value) => {
            return moment.duration(value).asSeconds();
          }
        },
        {
          key: 'testResult',
          sortable: false
        }
      ],
      testCases: []
    }
  }
}
</script>

<style>

.stacktrace-container {
  margin-top: 2.5em;
}

.stacktrace {
  overflow-y: scroll;
  white-space: nowrap;
}
</style>
