#!/usr/bin/expect
##https://www.jianshu.com/p/045f95c008a0
##1. 创建根证书密钥文件(自己做CA) root.key 
spawn openssl genrsa -des3 -out root.key 2048
 expect  {
       "*Enter pass phrase*" {send "123456\r"; exp_continue;}
       "*Verifying - Enter pass phrase*" {send "123456\r"} 
       "*#" { send "exit\r" } 
 }

##2. 创建根证书的申请文件 root.csr
 spawn openssl req -new -key root.key -out root.csr
 expect  {
       "*Enter pass phrase*" {send "123456\r"; exp_continue;}
       "*Country Name*" {send "CN\r"; exp_continue;}
       "*Province Name*" {send "BeiJing\r"; exp_continue;}  
       "*Locality Name*" {send "BeiJing\r"; exp_continue;}    
       "*Organization Name*" {send "BeiJing\r"; exp_continue;}
       "*Organizational Unit Name*" {send "BeiJing\r"; exp_continue;}
       "*Common Name*" {send "192.168.26.239\r"; exp_continue;}  
       "*Email Address*" {send "BeiJing@126.com\r"; exp_continue;}
       "*challenge password*" {send "123456\r"; exp_continue;} 
       "*optional company name*" {send "123456\r"}
       "*#" { send "exit\r" }
 }


##3. 创建一个自当前日期起为期十年的根证书 root.crt
spawn openssl x509 -req -days 3650 -sha256 -extfile /etc/pki/tls/openssl.cnf -extensions v3_ca -signkey root.key -in root.csr -out root.crt
expect  {
       "*Enter pass phrase for root*" {send "123456\r"}
       "*#" { send "exit\r" }
 }



##1.创建服务器证书密钥 server.key
spawn openssl genrsa -des3 -out server.key 2048
 expect  {
       "*Enter pass phrase*" {send "123456\r"; exp_continue;}
       "*Verifying - Enter pass phrase*" {send "123456\r"}  
       "*#" { send "exit\r" }
 }


##2.创建服务器证书的申请文件 server.csr
 spawn openssl req -new -key server.key -out server.csr
 expect  {
       "*Enter pass phrase*" {send "123456\r"; exp_continue;}
       "*Country Name*" {send "CN\r"; exp_continue;}
       "*Province Name*" {send "BeiJing\r"; exp_continue;}  
       "*Locality Name*" {send "BeiJing\r"; exp_continue;}    
       "*Organization Name*" {send "BeiJing\r"; exp_continue;}
       "*Organizational Unit Name*" {send "BeiJing\r"; exp_continue;}
       "*Common Name*" {send "192.168.26.239\r"; exp_continue;}  
       "*Email Address*" {send "BeiJing@126.com\r"; exp_continue;}
       "*challenge password*" {send "123456\r"; exp_continue;} 
       "*optional company name*" {send "123456\r"}
       "*#" { send "exit\r" }
 }


##3.创建自当前日期起有效期为期十年的服务器证书 server.crt
 spawn openssl x509 -req -days 3650 -sha256 -extfile /etc/pki/tls/openssl.cnf -extensions v3_req -CA root.crt -CAkey root.key -CAcreateserial -in server.csr -out server.crt
 expect  {
       "*Enter pass phrase for root*" {send "123456\r"}
       "*#" { send "exit\r" }
 }

##4.导出.p12文件 server.p12
 spawn openssl pkcs12 -export -in ./server.crt -inkey ./server.key -out  ./ca/server.p12 -name "tomcat"
 expect  {
       "*Enter pass phrase for*" {send "123456\r"; exp_continue;}
       "*Enter Export Password*" {send "123456\r"; exp_continue;}
       "*Verifying - Enter Export Password*" {send "123456\r"}
       "*#" { send "exit\r" }
 }

