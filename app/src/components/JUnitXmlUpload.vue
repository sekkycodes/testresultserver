<template>
  <div id="junit-xml-upload">
    <button class="btn btn-primary" data-toggle="modal" data-target="#junitUploadModal">
      Upload JUnit XML
    </button>

    <!-- Modal -->
    <div class="modal fade" id="junitUploadModal" tabindex="-1" aria-labelledby="junitUploadModalLabel"
         aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="junitUploadModalLabel">JUnit XML Upload</h5>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label for="fileSelectionInput">File Selection</label>
              <input class="form-control-file" id="fileSelectionInput" type="file" multiple="multiple" name="file"
                     data-overwrite-initial="false" data-min-file-count="1" ref="fileSelection">
              <small class="form-text text-muted">Select a JUnit XML file to upload.</small>
            </div>
            <div class="form-group">
              <label for="project">Project</label>
              <input id="project" class="form-control" type="text" v-model="project"/>
              <small class="form-text text-muted">The project for which the tests were executed.</small>
            </div>
            <div class="form-group">
              <label for="environment">Environment</label>
              <input id="environment" class="form-control" type="text" v-model="environment"/>
              <small class="form-text text-muted">For example "DEV", "TEST", "PROD", ...</small>
            </div>
            <div class="form-group">
              <label for="testType">Test Type</label>
              <input id="testType" class="form-control" type="text" v-model="testType"/>
              <small class="form-text text-muted">For example "Unit", "Integration", "E2E", ...</small>
            </div>
            <div class="form-group">
              <label for="executionTimestamp">Execution Timestamp (optional)</label>
              <input id="executionTimestamp" class="form-control" type="text" v-model="executionTime"/>
              <small class="form-text text-muted">Will use timestamp in JUnit XML if present, otherwise falls back to
                using current time.</small>
            </div>
            <div class="form-group">
              <label for="labelInput">Labels (optional)</label>
              <input id="labelInput" class="form-control" type="text" v-model="labels"/>
              <small class="form-text text-muted">Space separated free-text labels by which to later search and / or
                aggregate</small>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="onUploadClicked">Upload</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "JUnitXmlUpload",
  methods: {
    onUploadClicked: function () {
      this.$refs.fileSelection.files.forEach(f => this.uploadFile(f));
    },
    uploadFile: function (file) {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("project", this.project);
      formData.append("environment", this.environment);
      formData.append("executionTimeStamp", this.executionTime.toString());
      formData.append("testType", this.testType);
      formData.append("labels", this.labels);
      axios.post("http://localhost:8081/api/result-import/import-junit", formData)
          .then(function (result) {
            console.log(result);
          }, function (error) {
            console.log(error);
          });
    }
  },
  data() {
    return {
      environment: "",
      project: "",
      testType: "",
      executionTime: Date.now(),
      labels: ""
    }
  }
}
</script>
