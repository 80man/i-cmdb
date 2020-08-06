#!/bin/bash
JAVA_HOME=/soft/jdk1.8.0_191 ; export JAVA_HOME
if [[ ! -d ./ca ]]; then
	mkdir ca
fi
rm -rf ./ca/*

./openssl_install.sh
./openssl_install2.sh
./openssl_install3.sh
rm -rf  server.* root.*
