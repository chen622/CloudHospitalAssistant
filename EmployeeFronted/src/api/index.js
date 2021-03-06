import axios from 'axios'
import vue from '../main'


// 返回在vue模板中的调用接口
export default { // 自定义判断元素类型JS
    url: "http://localhost:8010",
    // url: "https://his.ccm.ink:8888",
    toType: function (obj) {
        return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
    }, // 参数过滤函数
    filterNull: function (o) {
        for (let key in o) {
            if (o[key] === null) {
                delete o[key]
            }
            if (this.toType(o[key]) === 'string') {
                o[key] = o[key].trim()
            } else if (this.toType(o[key]) === 'object') {
                o[key] = this.filterNull(o[key])
            } else if (this.toType(o[key]) === 'array') {
                o[key] = this.filterNull(o[key])
            }
        }
        return o
    },
    /*
      接口处理函数
      自动附加 IP 和 Token
    */
    apiAxios: function (method, url, params, success, failure, cacheable) {
        if (typeof cacheable === 'undefined') {
            cacheable = false
        }
        if (params) {
            params = this.filterNull(params)
        }
        let token = sessionStorage.getItem('token')

        axios({
            method: method,
            url: this.url + url,
            data: method === 'POST' || method === 'PUT' ? params : null,
            params: method === 'GET' || method === 'DELETE' ? params : null,
            baseURL: this.url,
            headers: {
                'Authorization': token,
                'Content-Type': 'application/json;charset=UTF-8'
            },
            withCredentials: false
        }).then(function (res) {
            if (res.data.code === 401) {
                location.href = '/#/login'
            }
            if (url === "/user/login")
                success(res)
            if (success) {
                success(res.data)
            }
            // console.log(JSON.stringify(res.data))
        }).catch(function (err) {
            if (err) {
                if (err) {
                    if (url !== "/user/login" && err.response && err.response.status === 403) {
                        sessionStorage.removeItem("token")
                        location.href = '/#/login'
                        vue.$message.destroy()
                        vue.$message.error("登录超时")
                    } else {
                        vue.$message.destroy()
                        vue.$message.error("网络异常")
                        // eslint-disable-next-line
                        console.log('API error: ' + err)
                        failure(err)
                    }
                }
            }
        })
    },
    get: function (url, params, success, failure, cacheable) {
        return this.apiAxios('GET', url, params, success, failure, cacheable)
    },
    post: function (url, params, success, failure, cacheable) {
        return this.apiAxios('POST', url, params, success, failure, cacheable)
    },
    put: function (url, params, success, failure, cacheable) {
        return this.apiAxios('PUT', url, params, success, failure, cacheable)
    },
    delete: function (url, params, success, failure, cacheable) {
        return this.apiAxios('DELETE', url, params, success, failure, cacheable)
    },
}
