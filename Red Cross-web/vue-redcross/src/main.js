import Vue from 'vue'
import App from './App.vue'
import router from './router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);

// 添加全局日期格式化过滤器
Vue.filter('formatDate', function(value) {
  if (!value) return '';
  const date = new Date(value);
  return date.toLocaleString();
});

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
