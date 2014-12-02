####Nginx配置文件结构

位于Nginx安装目录的conf目录下，整个配置文件以block形式组织。每个block一般以"{}"来标示，block可以分为几个层次，
整个配置文件中main（全局设置）位于最高层，在mian层下面可以有Events、Http。在Http层中有server（主机设置）层，server层有可以分为location（URL匹配特定位置的设置）层。

main：设置将影响所有其他所有设置
server：设置主要用于指定主机和端口
upstream：主要用于负载均衡，设置一系列的后端服务器
location：用于匹配网页位置
server继承main，location继承server，upstream既不会继承其他也不会被继承

1 Nginx的全局配置

```
user nobody nobody;
worker_processes 4;
error_log logs/error.log notice;
pid logs/nginx.pid;
worker_rlimit_nofile 65535;
events{
    use epoll;
    worker_connections 65536;
}
```

- user是主模块指令，指定Nginx worker进程运行用户以及用户组，默认是由nobody账号运行。
- worker_processes是主模块指令，指定了Nginx要开启的进程数。每个Nginx平均耗费10MB~12MB。一般指定一个进程就足够了，如果是多核CPU，建议指定和CPU的数量一样多的进程数即可。
- error_log 是主模块指令，用来定义全局错误日志文件。输出级别：debug info notice warn error crit。
- pid 是主模块指令，用来指定进程id的存储文件位置。
- worker_rlimit_nofile 用于绑定worker进程和CPU，Linux内核2.4以上可用。
- events用来指定Nginx的工作模式及连接数上限。
- use是事件模块指令，用来指定Nginx的工作模式。
- worker_connections 是时间模块指令，用于定义Nginx每个进程的最大连接数，默认是1024。最大客户端连接数由worker_processes和worker_connections决定，即：
    max_clients=worker_processes * worker_connections,在作为反向代理时变为：max_clients=worker_processes * worker_connections/4
  进程的最大连接数受Linux系统进程的最大打开文件数限制，在执行操作系统命令"ulimit -n 65536" 后worker_connections的设置才能生效。
  
2 Http服务器配置

```
http{
    include  conf/mime/types;
    default_type  application/octet-stream;
    log_format main '$remote_addr - $remote_user [$time_local]'
        ' "$request" $status $bytes_sent'
        ' "$http_refer" "$http_user_agent"'
        ' "$gzip_ratio"'
    log_format download '$remote_addr - $remote_user [$time_local]'
        ' "$request" $status $bytes_sent'
        ' "$http_refer" "$http_user_agent"'
        ' "$http_range" "$sent_http_content_range"'
    client_max_body_size 20m;
    client_header_buffer_size 32k;
    large_client_header_buffers 4 32k;
    sendfile on;
    tcp_nopush  on;
    tcp_nodelay on;
    keepalive_timeout 60;
    client_header_timeout 10;
    client_body_timeout 10;
    send_timeout 10;
}
```

- include 是主模块指令，实现对配置文件所包含的文件的指定，可以减少配置文件的复杂度。
- default_type 是Http核心模块指令，这里设定默认为二进制流，也就是当文件类型未定义时使用这种方式。例如在没有配置php环境时，Nginx是不予解析的
  此时，用浏览器访问php文件就会出现下载窗口
- log_format 是Nginx的HttpLog模块指令，用于指定Nginx日志的输出格式。main为此日志输出格式的名称，可以在下面的access_log指令中引用
- client_max_body_size 用来设置允许客户端请求的最大单个文件字节数
- client_header_buffer_size 用于指定来自客户端请求头的headerbuffer大小。
- large_client_header_buffers 用来指定客户端请求中较大的消息头的缓存最大数量和大小，“4”为个数，“32k”为大小。
- sendfile 用于开启高效文件传输模式。将tcp_nopush和tcp_nodelay设置为on，用户防止网络阻塞。
- keepalive_timeout用户设置客户端链接保持活动的超时时间。
- client_header_timeout 用户设置客户端请求头读取超时时间。如果超过这个时间，客户端没有发送任何数据，Nginx将返回“Request time out(408)”。
- client_body_timeout 用于设置客户端请求主体读取超时时间，默认为60.如果超过，则返回“Request time out(408)”。
- send_timeout 用户指定响应客户端的超时时间。

3 HttpGzip模块配置

支持在线实时压缩输出数据了。查看是否按照了此模块：/nginx/sbin/nginx -V  查看安装Nginx时的编译选项。由输出可以看出是否安装了HttpGzip模块。

```
gzip on;
gzip_min_length 1k;
gzip_buffers 4 16k;
gzip_http_version 1.1;
gzip_comp_level 2;
gzip_types text/plain application/x-javascript text/css application/xml;
gzip_vary on;
```

- gzip 用户开启或者关闭gzip模块。
- gzip_min_length 用于设置允许压缩的页面最小字节数，页面字节数是从header头中的Content-Length中获取。默认是0，不管页面多大都进行压缩。建议设置成大于1k的字节数。
- gzip_buffers 表示申请4个单位为16k的内存作为压缩结果流缓存。默认值是申请与原始数据大小相同的内存空间来存储gzip压缩结果
- gzip_http_version 用户设置识别http协议版本，默认是1.1
- gzip_comp_level 用于指定gzip压缩比，1压缩比最小，处理速度最快；9压缩比最大，传输速度快，但处理最慢，也比较耗资源。
- gzip_types 用于指定压缩的类型。无任是否指定，"text/html"类型总是会被压缩的。
- gzip_vary 可以让前端的缓存服务器缓存警告gzip压缩的页面。

4 负载均衡配置

```
upstream indeR{
    ip_hash;
    server 192.168.12.153:80;
    server 192.168.12.154:80 down;
    server 192.168.12.155:80 max_fails=3 fail_timeout=20s;
    server 192.168.12.166:80;
}
```

upstream 是Nginx的Http Upstream模块，这个模块通过一个简单的调度算法来实现客户端Ip到后端服务器的负载均衡。Nginx的负载均衡目前支持4种调度算法：
- 轮询（默认）。每个请求按时间顺序逐一分配到不同的后端服务器，如果后端某台服务器宕机，故障系统被自动剔除，使用户访问不受影响。
- Weight。指定轮询权值，Weight值越大，分配的访问几率越高。
- ip_hash.每个请求按访问IP的hash结果分配，这样来自同一个IP的访客固定访问同一个后端服务器，有效解决了动态网页存在的session共享问题。
- fair。比上面更加智能的负载均衡算法。可以依据页面大小和加载时间长短智能的进行负载均衡，也就是根据后端服务器的响应时间来分配请求，响应时间短的优先分配。Nginx本身不支持
    fair。需要下载upstream_fair模块
- url_hash。按访问的url的hash结果来分配请求。Nginx本身不支持。

- server。指定后台服务器的IP地址和端口，同时还可以设定每个后端服务器负载均衡调度中的状态。常用的状态：
   1 down，表示当前的server暂时不参与负载均衡。
   2 backup，预留的备份机器。当其他所有的非backup的机器出现故障或者忙的时候，才会请求backup机器。
   3 max_fails，允许请求失败的次数，默认为1.当超过最大次数时，返回proxy_next_upstream
   4 fail_timeout,在经历了max_fails次数失败后，暂停服务的时间。
   
5 server虚拟主机配置
```
server{
listen 80;
server_name 192.168.12.162  www.xxx.com;
index index.html index.htm index.jsp;
root /web/wwwroot/www.xxx.com/
charset gb2312;
```

- server标志定义虚拟主机开始。
- listen用于指定虚拟主机的服务器端口。
- server_name用来指定ip地址或者域名，多个域名之间用空格分开
- index 用于设置默认访问的首页地址
- root 用于指定虚拟主机的网页根目录，这个目录可以是相对路径，也可以是绝对路径
- charset 用户设置网页的默认编码格式
- access_log logs/www.xx.log main  用于指定此虚拟主机的访问日志存放路径。最后的main用于指定日志的输出格式。

6 URL匹配配置
location支持正则表达式匹配，也支持条件判断匹配，用户可以通过location指令实现Nginx对动、静态网页的过滤处理。

```
location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$ {
    root /web/wwwroot/www.xxx.com;
    expires 30d;
}
```

所有扩展名为gif|jpg|jpeg|png|bmp|swf交给Nginx处理，expires指定过期时间为30天

```
location ~ .*.jsp${
    index index.jsp;
    proxy_pass http://localhost:8080;
}
```

location 对此虚拟机下的动态网页过滤处理，将所有已.jsp为后缀的文件都交给本机的8080端口处理。

7 StubStatus模块配置

StubStatus模块能够获取Nginx自上次启动以来的工作状态，此模块非核心模块，需要在Nginx编译安装时手工指定才能使用。

```
location /NginxStatus{
    stub_status on;
    access_log logs/NginxStatus.log;
    auth_basic "NginxStatus";
    auth_basic_user_file ../htpasswd;
}
```

- stub_status表示启用StubStatus的工作状态统计功能
- access_log 用来指定StubStatus模块的访问日志文件
- auth_basic 是Nginx的一种认证机制
- auth_basic_user_file 用来指定认证密码文件。


```
error_page 404  /404.html;
error_page 500 502 503 504 /50x.html;
location = /50x.html{
    root html;
}
}
```

- error_page 定制各种错误信息的返回页面。注意：这些错误信息的返回页面大小一定要超过512KB，不然会被IE替换为IE默认的错误页面。


####Nginx的启动、关闭和平滑重启

假设Nginx安装在/usr/local/nginx目录下

1 检查配置文件的正确性：/usr/local/nginx/sbin/nginx -t  或者 /usr/local/nginx/sbin/nginx -t -c /usr/local/nginx/conf/nginx.conf

2 显示Nginx的版本已经相关编译信息：/usr/local/nginx/sbin/nginx -V

3 启动：/usr/local/nginx/sbin/nginx
4 关闭：关闭Nginx进程 kill -xxx pid xxx是信号名，pid是Nginx的进程号，可以通过如下两个命令或者：

```
ps -ef | grep "nginx:master process" | grep -v "grep" | awk -F ' ' '${print $2}'

cat /usr/local/nginx/logs/nginx.pid
```

信号名有 **`QUIT`**  关闭进程  **`HUP`** 重新加载配置  **`USR1`** 用于Nginx日志切换，重新打开一个日志文件  **`USR2`** 平滑升级可执行程序

5 平滑重启：` kill -HUP cat /usr/local/nginx/logs/nginx.pid `


#####Nginx 负载均衡配置示例

```
http{
    upstream myServer{
        ip_hash;
        server 192.168.12.153:80;
        server 192.168.12.154:80 down;
        server 192.168.12.155:80 max_fails=3 fail_timeout=20s;
        server 192.168.12.166:80;
    }
    
    server {
        listen 80;
        server_name wwww.domain.com 192.168.12.162;
        index index.html index.htm;
        root /www/web/webroot;
        
        location / {
            proxy_pass http://myserver;
            proxy_next_upstream http_500 http_502 http_503 error timeout invalid_header;
            proxy_redirect off;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For %proxy_add_x_forwarded_for;
            client_body_buffer_size 128k;
            proxy_connect_timeout 90;
            proxy_send_timeout 90;
            proxy_read_timeout 90;
            proxy_buffer_size 4k;
            proxy_buffers 4 32k;
            proxy_busy_buffers_size 64k;
            proxy_temp_file_write_size 64k;
        }
    }
}
```

在上面的示例中，先定义了一个负载均衡组myServer，然后在location部分通过"proxy_pass http://myserver;"实现负载调度功能。proxy_pass可以用来指定
代理的后端服务器地址和端口地址可以是主机名或者IP地址，也可以是通过upstream指令设定的负载均衡组名称。

- proxy_next_upstream 用来定义故障转移策略，当后端服务节点返回500 502 503 504 和执行超时等错误时，自动请求转发到upstream负载均衡组的另一头服务器，实现故障转移。
- proxy_set_header 设置由后端服务器获取用户的主机名或真实IP地址，以及代理者的真实IP地址。
- client_body_buffer_size 用于指定客户端请求缓冲区大小
- proxy_connect_timeout 表示与后端服务器连接的超时时间
- proxy_send_timeout 表示后端服务器的数据回传时间
- proxy_read_timeout 设置Nginx从代理的后端服务器获取信息的时间，表示连接建立成功后，Nginx等待后端服务器响应的时间
- proxy_buffer_size 设置缓冲区大小
- proxy_buffers 设置缓冲区的数量和大小
- proxy_busy_buffers_size 用于设置系统很忙时可以使用的proxy_buffers大小
- proxy_temp_file_write_size 指定proxy缓存临时文件的大小


####防盗链配置实例

```
location ~* \.(jpg|gif|png|swf|flv|wma|wmv|asf|mp3|mmf|zip|rar)$ {
    valid_referers none blocked *.domain1.com domain.com;
    if($invalid_referer){
        rewrite ~/ http:www.domain.com/img/error.gif;
        #return 403;
    }
}
location /images{
    root /data/www/root;
    valid_referers none blocked *.domain1.com domain.com;
    if($invalid_referer){
            rewrite ~/ http:www.domain.com/img/error.gif;
            #return 403;
    }
}
```

if()中的内容表示：如果地址不是上面地址的地址就跳转到通过rewrite指定的地址也可以通过return返回403错误


####日志分隔配置实例

Nginx对日志进行处理的脚本。

```
#/bin/bash

savepath_log='/home/nginx/logs'
nglogs='/usr/local/nginx/logs'

mkdir -p $savepath_log/$(date +%Y)/$(date +%m)
mv $nglogs/access.log $savepath_log/$(date +%Y)/$(date +%m)/access.$(date +%Y%m%d).log
mv $nglogs/error.log  $savepath_log/$(date +%Y)/$(date +%m)/error.$(date +%Y%m%d).log
kill -USR1 `/usr/local/nginx/logs/nginx.pid `
```

把这段脚本放到Linux的crontab中，每天0点执行，就可以实现日志的每天分隔功能。


###Nginx性能优化技巧

1 编译安装过程优化：

 减小Nginx编译后的文件大小。编译Nginx的时候取消debug模式运行。在源码目录找到auto/cc/gcc文件，找到

```
# debug
CFlags=" $CFLAGS -g"
```

注释掉或者删掉这两行。

2 利用TCMalloc优化Nginx性能

3 Nginx内核参数优化


####Nginx与Java环境

Nginx与Tomcat：

Nginx可以通过以下两种方式与Tomcat耦合：

1 将静态页面请求交给Nginx，动态请求交给后端Tomcat处理

2 将所有请求都交给后端的tomcat服务器处理，通过利用Nginx自身的负载均衡功能进行多台Tomcat服务器的负载均衡

#####第一种配置：

```
server{
    listen 80;
    server_name www.xxx.com;
    root /web/www/html;
    location /img/{
        alias /web/www/html/img/;
    }
    
    location ~ (\.jsp)|(\.do)${
        proxy_pass http://192.168.12.156:8080;
        proxy_redirect off;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For %proxy_add_x_forwarded_for;
        client_body_buffer_size 128k;
        proxy_connect_timeout 90;
        proxy_send_timeout 90;
        proxy_read_timeout 90;
        proxy_buffer_size 4k;
        proxy_buffers 4 32k;
        proxy_busy_buffers_size 64k;
        proxy_temp_file_write_size 64k;
    }

}
```

首先定义一个虚拟主机www.xxx.com，然后通过location指令将/web/www/html/img/目录下的静态文件交给Nginx来完成。最后一个location指令将所有以.jsp或者.do结尾的文件都交给
Tomcat服务器的8080端口来处理，即：http://192.168.12.156:8080。

**注意：在location指令中使用正则表达式后，proxy_pass后面的代理路径不能还有地址链接，也就是不能写成proxy_pass http://192.168.12.156:8080/,后者类似proxy_pass http://192.168.12.156:8080/jsp的形式
在location没有使用正则表达式的时候，没有此限制**

#####多个tomcat负载均衡实例

假定有3台tomcat服务器，开发不同的端口如下：

```
192.168.12.156:8080
192.168.12.157:8081
192.168.12.158:8082
```

Nginx 配置如下：

```
upstream mytomcats{
    server 192.168.12.156:8080;
    server 192.168.12.157:8081;
    server 192.168.12.158:8082;
}

server{
    listen 80;
    server_name www.xxx.com;
    location ~* \.(jpg|gif|png|swf|flv|wma|wmv|asf|mp3|mmf|zip|rar){
        root /web/www/html/;
    }
    
    location / {
        proxy_pass http://mytomcats;
        proxy_redirect off;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For %proxy_add_x_forwarded_for;
        client_body_buffer_size 128k;
        proxy_connect_timeout 90;
        proxy_send_timeout 90;
        proxy_read_timeout 90;
        proxy_buffer_size 4k;
        proxy_buffers 4 32k;
        proxy_busy_buffers_size 64k;
        proxy_temp_file_write_size 64k;
    }
}
```

**注意：如果在location指令使用正则表达式后在用alias指令，Nginx是不支持的**


> 引用自：高性能Linux服务器构建实战