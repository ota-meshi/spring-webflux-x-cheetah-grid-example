import Vue from "vue";
import VueRouter from "vue-router";
import SampleCGrid from "../components/SampleCGrid.vue";
import SampleCheetahGrid from "../components/SampleCheetahGrid.vue";

const router = new VueRouter({
  routes: [
    {
      path: "/",
      component: SampleCheetahGrid,
      name: "basic",
      meta: {
        target: "Cheetah Grid",
        link: "https://github.com/future-architect/cheetah-grid"
      }
    },
    {
      path: "/c-grid",
      component: SampleCGrid,
      name: "forVue",
      meta: {
        target: "vue-cheetah-grid",
        link: "https://www.npmjs.com/package/vue-cheetah-grid"
      }
    }
  ]
});

export const routerMeta = new Vue({
  data() {
    return {
      target: null,
      link: null
    };
  }
});
router.afterEach(to => {
  routerMeta.target = to.meta.target;
  routerMeta.link = to.meta.link;
});

export default router;
