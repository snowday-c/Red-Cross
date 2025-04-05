import axios from 'axios'

// 创建axios对象
const request = axios.create({
    baseURL: 'https://120.27.161.155:8090/api',//服务器环境
    //  baseURL: 'http://localhost:8090/api',//本地环境
    timeout: 5000
})

// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    const CurrentUser = localStorage.getItem("CurrentUser");
        if (CurrentUser) {
            config.headers['token'] = JSON.parse(CurrentUser).token;
        }

    return config
}, error => {
    return Promise.reject(error)
});

// // response 拦截器
// // 可以在接口响应后统一处理结果
// request.interceptors.response.use(
//     response => {
//         // response.data即为后端返回的Result
//         let res = response.data;
//         // 兼容服务端返回的字符串数据
//         if (typeof res === 'string') {
//             res = res ? JSON.parse(res) : res
//         }
//         return res;
//     },
//     error => {
//         console.log('err' + error) // for debug
//         return Promise.reject(error)
//     }
// )


export default request