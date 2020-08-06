#!/bin/bash
##5.将.p12 文件导入到keystore JKS文件 server.keystore
$JAVA_HOME/bin/keytool -importkeystore -v -srckeystore  ./ca/server.p12 -srcstoretype pkcs12 -srcstorepass 123456 -destkeystore ./ca/server.keystore -deststoretype jks -deststorepass 123456
