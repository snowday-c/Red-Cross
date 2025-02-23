import Vue from 'vue';
import Router from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Management from '../views/Management.vue';

import Dashboard from '../views/Dashboard.vue';
import Users from '../views/Users.vue';
import Messages from '@/views/Messages.vue';
import Questions from '@/views/Questions.vue';
import Trains from '@/views/Trains.vue';

Vue.use(Router);

const routes = [
  {
    path: '/',
    redirect: '/login', // 默认重定向到登录页面
  },
  {
    path: '/login',
    component: Login,
  },
  {
    path: '/register',
    component: Register,
  },
  {
    path: '/management',
    component: Management,
    meta: { requiresAuth: true }, // 需要登录才能访问
    children: [
      {
        path: 'dashboard',
        component: Dashboard,
      },
      {
        path: 'users',
        component: Users,
      },
      {
        path: 'messages',
        component: Messages,
      },
      {
        path: 'questions',
        component: Questions,
      },
      {
        path: 'trains',
        component: Trains,
      },
      {
        path: '',
        redirect: 'dashboard', // 默认重定向到
      }
    ]
  }
];

const router = new Router({
  mode: 'history',
  routes,
});

// 全局前置守卫
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = checkAuth(); // 检查用户是否登录
//   if (to.meta.requiresAuth && !isAuthenticated) {
//     next('/login'); // 如果未登录，跳转到登录页面
//   } else {
//     next(); // 否则继续导航
//   }
// });

// // 模拟检查用户是否登录的函数
// function checkAuth() {
//   // 这里可以根据实际情况从 localStorage 或 Vuex 中获取登录状态
//   return localStorage.getItem('isLoggedIn') === 'true';
// }

export default router;
