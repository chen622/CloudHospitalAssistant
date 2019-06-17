# Cloud Hospital Assistant
### 开发环境

- 使用 Intellij IDEA 2019.1.3 以上版本作为集成开发环境
- 使用 Java 1.8 以上的版本作为编译器
- 使用 Maven 3.3.9 以上版本作为项目构建工具
- 使用 MySQL 8.0.16 以上版本作为主数据库
- 使用 Redis 5.0.5 以上版本作为缓存数据库
- 使用 Node.js 11.13.0 以上版本作为前端运行环境

### 开发手册

> 搭建完以上环境后即可进行开发

1. 解压 SourceCode.zip 文件
2. 使用 Intellij IDEA 打开解压后的文件夹
3. 使用 Maven 引入 ./CloudHospitalAssistant/Backend/pom.xml 文件，并下载对应依赖
4. 在 MySQL 数据库中执行 init.sql 脚本，搭建数据库
5. 下载完成后修改 ./CloudHospitalAssistant/Backend/src/main/resources/application.properties 文件，将MySQL 和 Redis 的配置项修改为自主搭建的数据库
6. 后端至此即可运行，访问地址为: http://localhost:8878

![后端图例](https://i.loli.net/2019/06/16/5d061f10cd11b19844.png)

7. 医院员工端需使用 node.js 运行

8. 切换至前端对应的目录 ./CloudHospitalAssistant/EmployeeFronted/

9. 使用控制台下载依赖

   ` $ npm install`

10. 下载完成后执行运行命令

    `$ npm run serve`

11. 前端至此即可运行，访问地址为: http://localhost:8888

![前端图例](https://i.loli.net/2019/06/16/5d061f117eef942608.png)

### 部署手册

1. 开发环境搭建成功后，即可进行部署

2. 切换至后端对应的目录 ./CloudHospitalAssistant/Backend/，运行以下命令

   ` maven package -Dmaven.test.skip=true `

3. 切换至编译结果目录 ./CloudHospitalAssistant/Backend/target/，将打包后的 his-0.0.1-SNAPSHOT.jar 传输至服务器上

4. 使用如下命令运行

   `java -jar his-0.0.1-SNAPSHOT.jar ` 

5. 切换至前端对应的目录 ./CloudHospitalAssistant/EmployeeFronted/，运行如下命令

   `npm run build`

6. 切换至打包结果目录 ./CloudHospitalAssistant/EmployeeFronted/dist，拷贝 dist 文件夹到服务器上

   > 由于 WebPack 包可使用多种服务器进行构建，以下使用 nginx 作为实例

7. 部署前首先确认服务器已成功安装 1.16 版本以上的 nginx

8. 拷贝 dist 文件夹 至 /home 目录下

9. 在 nginx.conf 文件中设置端口转发，具体配置如下

   ```bash
   
   #user  nobody;
   user www www;
   worker_processes  1;
   
   error_log /usr/local/webserver/nginx/logs/nginx_error.log crit; #日志位置和日志级别
   pid /usr/local/webserver/nginx/nginx.pid;
   #error_log  logs/error.log;
   #error_log  logs/error.log  notice;
   #error_log  logs/error.log id        logs/nginx.pid;
   worker_rlimit_nofile 1024;
   events{
   	use epoll;  
   	worker_connections  1024;
   }
   
   
   http {
       include       mime.types;
       default_type  application/octet-stream;
   
       log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                         '$status $body_bytes_sent "$http_referer" '
                         '"$http_user_agent" "$http_x_forwarded_for"';
   
       #access_log  logs/access.log  main;
   
       sendfile        on;
       tcp_nopush     on;
   
       #keepalive_timeout  0;
       keepalive_timeout  65;
   
       gzip  on;
       gzip_min_length 1k;
       gzip_buffers 4 16k;
       gzip_http_version 1.0;
       gzip_comp_level 2;
       gzip_types text/plain application/javascript  text/css application/xml application/x-javascript text/javascript ;
       gzip_vary on;
   	
   	server {
   	    listen 80;
   	    server_name his.ccm.ink;
   
           location / {
               root   /home/dist/;
               index  index.html index.htm;
           }
   
           error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
   
   	}
   
   
       server {
           listen       8878;
           server_name  his.ccm.ink;
   
           location @backend {
           proxy_set_header X-Forwarded-For $remote_addr;
           proxy_set_header Host            $http_host;
    
           proxy_pass http://127.0.0.1:8878;
           }
       }
   }
   
   
   ```
