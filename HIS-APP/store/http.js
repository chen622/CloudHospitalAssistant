const baseUrl = 'http://localhost:8020';


//带Token请求
const httpTokenRequest = (opts, data) => {
	let token = uni.getStorageSync('token');
	let httpDefaultOpts = {
		url: baseUrl + opts.url,
		data: data,
		method: opts.method,
		header: {
			'Authorization': token,
			"Accept": "application/json",
			"Content-Type": "application/json; charset=UTF-8"
		},
		dataType: 'json',
	}

	let promise = new Promise(function(resolve, reject) {
		uni.request(httpDefaultOpts).then(
			(res) => {
				resolve(res[1])
			}
		).catch(
			(response) => {
				reject(response)
			}
		)
	})
	return promise
};


export default {
	baseUrl,
	httpTokenRequest
}
