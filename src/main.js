import Vue from "vue";
import VueRouter from "vue-router";
import vueCheetahGrid from "vue-cheetah-grid";
import router from "./router";
import App from "./App.vue";

Vue.use(VueRouter);
Vue.use(vueCheetahGrid);
Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
