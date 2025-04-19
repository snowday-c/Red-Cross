import Vue from 'vue';
import Router from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Management from '../views/Management.vue';
import Feedback from '../views/Feedback.vue';
import Users from '../views/Users.vue';
import Messages from '@/views/Messages.vue';
import Questions from '@/views/Questions.vue';
import Trains from '@/views/Trains.vue';
import Certificates from '@/views/Certificates.vue';
import ScoreAnalysis from '@/views/ScoreAnalysis.vue'

Vue.use(Router);

const routes = [
  {
    path: '/',
    redirect: '/login', // 默认重定向到登录页面
  },
  {
    path: '/login',
    component: Login,
    meta:{
      title:"登录页面"
  }
  },
  {
    path: '/register',
    component: Register,
    meta:{
      title:"注册页面"
  }
  },
  {
    path: '/management',
    component: Management,
    meta: { requiresAuth: true }, // 需要登录才能访问
    children: [

      {
        path: 'users',
        component: Users,
        meta:{
          title:"用户管理"
      }
      },
      {
        path: 'messages',
        component: Messages,
        meta:{
          title:"通知管理"
      }
      },
      {
        path: 'questions',
        component: Questions,
        meta:{
          title:"试题管理"
      }
      },
      {
        path: 'trains',
        component: Trains,
        meta:{
          title:"培训管理"
      }
      },
      {
        path: 'certificates',
        component: Certificates,
        meta:{
          title:"证书管理"
      }
      },
      {
        path: 'feedback',
        component: Feedback,
        meta:{
          title:"反馈处理"
      }
      },
      {
        path: '',
        redirect: 'users', // 默认重定向到
      },
      {
        path: '/score-analysis',
        component: ScoreAnalysis,
        meta: { 
          title: '成绩分析' 
        }
      }
    ]
  }
];

const router = new Router({
  mode: 'history',
  routes,
});

// 全局解析守卫
router.beforeResolve((to, from, next) => {
  window.document.title = to.meta.title
  next()
})

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
