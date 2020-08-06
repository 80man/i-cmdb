#!/usr/bin/expect
##迁移到行业标准格式 PKCS12
 spawn /soft/jdk1.8.0_191/bin/keytool -importkeystore -srckeystore ./ca/server.keystore -destkeystore ./ca/server.keystore -deststoretype pkcs12
 expect  {
       ":" {send "123456\r"; exp_continue;}
       "*#" { send "exit\r" }
 }