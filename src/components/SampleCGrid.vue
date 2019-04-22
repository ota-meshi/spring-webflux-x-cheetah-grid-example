<template>
  <div class="sample-c-grid">
    <div v-if="!data.length" class="button-wrapper">
      <button class="action-button" :disabled="requested" @click="onClick">
        Load 1,000,000 records
      </button>
      <br />
      <button :disabled="requested" @click="syncOnClick">
        Load 1,000,000 records (sync)
      </button>
    </div>
    <h4 class="status">
      {{ message }}
      <template v-if="initTimeMs || totalTimeMs">
        [
        <template v-if="initTimeMs">
          init: {{ initTimeMs }}ms
        </template>
        <template v-if="totalTimeMs">
          total: {{ totalTimeMs }}ms
        </template>
        ]
      </template>
    </h4>
    <div v-if="data.length" class="grid-wrapper">
      <c-grid ref="grid" :data="data" frozen-col-count="2">
        <c-grid-check-column field="check" width="50px" />
        <c-grid-column field="id" width="100px">
          ID
        </c-grid-column>
        <c-grid-column-group caption="Name">
          <c-grid-column field="fname" width="calc(30% - 90px)">
            First Name
          </c-grid-column>
          <c-grid-column field="lname" width="calc(30% - 90px)">
            Last Name
          </c-grid-column>
        </c-grid-column-group>
        <c-grid-column field="email" width="calc(40% - 90px)">
          Email
        </c-grid-column>
        <c-grid-button-column
          caption="SHOW"
          width="120px"
          @click="recordOnClick"
        >
          Button
        </c-grid-button-column>
      </c-grid>
    </div>
  </div>
</template>

<script>
import { streamJsonForVue } from "../stream-json";
import { cheetahGrid } from "vue-cheetah-grid";

export default {
  name: "SampleCGrid",
  components: {},
  data() {
    return {
      data: [],
      message: "",
      initTimeMs: "",
      totalTimeMs: "",
      requested: false
    };
  },
  methods: {
    onClick() {
      const data = [];
      const dataSource = (this.data = new cheetahGrid.data.CachedDataSource({
        get(index) {
          return data[index];
        },
        length: 0
      }));

      let buffer = [];
      this.message = "request...";
      this.requested = true;
      const startTime = Date.now();
      streamJsonForVue(this, "/api/persons", {}, rec => {
        buffer.push(rec);
        if (
          buffer.length >= 10000 ||
          (data.length < 10000 && buffer.length >= 1000) ||
          (data.length < 1000 && buffer.length >= 100)
        ) {
          data.push(...buffer);
          dataSource.length = data.length;
          buffer = [];
          this.message = "loading... (" + dataSource.length + "records)";
          this.totalTimeMs = Date.now() - startTime;
          if (!this.initTimeMs) {
            this.initTimeMs = this.totalTimeMs;
          }
        }
      }).then(() => {
        data.push(...buffer);
        dataSource.length = data.length;
        this.message = "";
        this.totalTimeMs = Date.now() - startTime;
      });
    },
    syncOnClick() {
      this.message = "request...";
      this.requested = true;
      const startTime = Date.now();
      fetch("/api/persons/sync")
        .then(response => response.json())
        .then(records => {
          this.data = new cheetahGrid.data.CachedDataSource({
            get(index) {
              return records[index];
            },
            length: records.length
          });
          this.message = "";
          this.totalTimeMs = Date.now() - startTime;
        });
    },
    recordOnClick(rec) {
      alert(JSON.stringify(rec, null, 2));
    }
  }
};
</script>

<style scoped></style>
