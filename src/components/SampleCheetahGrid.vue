<template>
  <div class="sample-c-grid">
    <div v-if="!requested" class="button-wrapper">
      <button class="action-button" :disabled="requested" @click="onClick">
        Load 1,000,000 records
      </button>
      <br />
      <button :disabled="requested" @click="syncOnClick">
        Load 1,000,000 records (sync)
      </button>
    </div>
    <div v-show="requested" ref="gridWrapper" class="grid-wrapper"></div>
    <h4 class="status">
      {{ message }}
    </h4>
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
      hasData: false,
      message: "",
      requested: false
    };
  },
  methods: {
    onClick() {
      this.message = "request...";
      this.requested = true;
      this.$nextTick(() => this.startGrid());
    },
    createGrid(records) {
      const grid = (this.grid = new cheetahGrid.ListGrid({
        parentElement: this.$refs.gridWrapper,
        header: [
          {
            field: "check",
            caption: "",
            width: 50,
            columnType: "check",
            action: "check"
          },
          { field: "id", caption: "ID", width: 100 },
          {
            /* multiple header */
            caption: "Name",
            columns: [
              {
                field: "fname",
                caption: "First Name",
                width: "calc(30% - 90px)"
              },
              {
                field: "lname",
                caption: "Last Name",
                width: "calc(30% - 90px)"
              }
            ]
          },
          { field: "email", caption: "Email", width: "calc(40% - 90px)" },
          {
            caption: "Button",
            width: 120,
            /* button column */
            columnType: new cheetahGrid.columns.type.ButtonColumn({
              caption: "SHOW"
            }),
            action: new cheetahGrid.columns.action.ButtonAction({
              action(rec) {
                alert(JSON.stringify(rec, null, 2));
              }
            })
          }
        ],
        frozenColCount: 2
      }));
      grid.records = records;
      return grid;
    },
    startGrid() {
      const records = [];
      const grid = this.createGrid(records);
      let buffer = [];
      console.time("initial_time");
      let first = true;
      streamJsonForVue(this, "/api/persons", {}, rec => {
        buffer.push(rec);
        if (buffer.length >= 1000) {
          if (first) {
            first = false;
            console.timeEnd("initial_time");
          }
          records.push(...buffer);
          grid.records = records;
          buffer = [];
        }
        this.message = "loading... (" + records.length + "records)";
      }).then(() => {
        records.push(...buffer);
        grid.records = records;
        this.message = "";
      });
    },
    syncOnClick() {
      this.message = "request...";
      this.requested = true;
      this.$nextTick(() => {
        const records = [];
        const grid = this.createGrid(records);
        console.time("initial_time");
        fetch("/api/persons/sync")
          .then(response => response.json())
          .then(records => {
            console.timeEnd("initial_time");
            grid.records = records;
            this.message = "";
          });
      });
    },
    recordOnClick(rec) {
      alert(JSON.stringify(rec, null, 2));
    }
  }
};
</script>

<style scoped></style>
