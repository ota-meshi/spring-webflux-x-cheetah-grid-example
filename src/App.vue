<template>
  <div id="app">
    <div class="header">
      <a
        href="https://github.com/future-architect/cheetah-grid"
        class="logo"
        target="_blank"
      >
        <img
          src="https://future-architect.github.io/cheetah-grid/documents/logo.png"
          alt="Cheetah Grid"
        />
        <span> View on GitHub </span>
        <svg
          version="1.1"
          width="16"
          height="16"
          viewBox="0 0 16 16"
          aria-hidden="true"
          class="octicon octicon-mark-github"
        >
          <path
            fill-rule="evenodd"
            d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"
          />
        </svg>
      </a>
      <div class="links">
        <router-link :to="{ name: 'basic' }" class="nav-item"
          >Cheetah Grid × ...</router-link
        >
        <router-link :to="{ name: 'forVue' }" class="nav-item"
          >vue-cheetah-grid × ...</router-link
        >
        <a
          href="https://github.com/ota-meshi/spring-webflux-x-cheetah-grid-example"
          class="nav-item"
          target="_blank"
        >
          <span> GitHub </span>
          <svg
            version="1.1"
            width="16"
            height="16"
            viewBox="0 0 16 16"
            aria-hidden="true"
            class="octicon octicon-mark-github"
          >
            <path
              fill-rule="evenodd"
              d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"
            />
          </svg>
        </a>
      </div>
    </div>
    <h1>
      <a class="nav-item" :href="metaLink" target="_blank">{{ metaTarget }}</a>
      × Spring WebFlux
    </h1>
    <router-view class="view-main"></router-view>
  </div>
</template>

<script>
import { routerMeta } from "./router";
export default {
  name: "App",
  computed: {
    metaTarget() {
      return routerMeta.target;
    },
    metaLink() {
      return routerMeta.link;
    }
  }
};
</script>

<style scoped>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 60px;
  height: calc(100vh - 60px);
  text-align: center;
}
.header {
  position: fixed;
  top: 10px;
  left: 30px;
  right: 30px;
  text-align: left;
  display: flex;
  align-items: center;
}
.logo {
  display: inline-flex;
  align-items: center;
  height: 2.2rem;
  vertical-align: top;
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}
.logo img {
  height: 2.2rem;
  min-width: 2.2rem;
  margin-right: 0.8rem;
  vertical-align: top;
}
.links {
  display: inline-flex;
  align-items: center;
  margin-left: auto;
}
.links > a {
  display: inline-flex;
  align-items: center;
  margin-left: 1.5rem;
  font-weight: 500;
}
a > svg {
  padding-left: 5px;
  color: #aaa;
  fill: #aaa;
}
a {
  color: #2c3e50;
  text-decoration: none;
}
a.nav-item:hover {
  margin-bottom: -2px;
  border-bottom: 2px solid #ffa143;
}
#app >>> .action-button {
  appearance: none;
  border: none;
  outline: none;

  cursor: pointer;
  text-transform: uppercase;

  font-size: 1.2rem;
  color: #fff;
  background-color: #f93;
  padding: 0.8rem 1.6rem;
  border-radius: 4px;
  transition: background-color 0.1s ease;
  box-sizing: border-box;
  border-bottom: 1px solid #ff8a14;
  font-weight: 500;
  text-decoration: none;
}
#app >>> .action-button:hover {
  background-color: #ffa347;
}
#app >>> .action-button[disabled] {
  opacity: 0.5;
}
#app >>> .grid-wrapper {
  padding: 10px;
  height: 100%;
  text-align: left;
  box-sizing: border-box;
}
#app >>> .cheetah-grid {
  border: 1px solid #ddd;
}
#app >>> .status {
  opacity: 0.6;
  position: absolute;
  top: 100px;
  right: 30px;
  left: 30px;
  text-align: center;
  margin: 0;
}
.view-main {
  height: calc(100% - 44px - 0.67em * 2);
}
</style>
