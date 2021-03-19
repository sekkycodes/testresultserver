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
      <div>{{ this.message }}</div>
      <div style="overflow-x:scroll" v-html="this.details">{{ this.details }}</div>
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
      return str.replace(/(?:\r\n|\r|\n)/g, '<br>');
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
          sortable: true,
          formatter: (value) => {
            const tempTime = moment.duration(value);
            return tempTime.seconds() + " sec";
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
