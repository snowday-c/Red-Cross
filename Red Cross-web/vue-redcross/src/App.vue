<template>
  <div id="app">
    <router-view></router-view> <!-- 直接渲染路由视图 -->
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin: 0;
  padding: 0;
}
</style>