#!/bin/sh

echo "*********** BEGIN BUILDING DEPLOYMENT PACKAGE ************"
mvn clean package
echo "*********** COMPLETED BUILDING DEPLOYMENT PACKAGE ************"

echo "*********** BEGIN CLEANING ECLIPSE PROJECT ************"
mvn eclipse:clean
echo "*********** COMPLETED CLEANING ECLIPSE PROJECT ************"

echo "*********** BEGIN BUILDING ECLIPSE PROJECT ************"
mvn eclipse:eclipse
echo "*********** COMPLETED BUILDING ECLIPSE PROJECT ************"

echo "*********** DEPLOYING TO TOMCAT SERVER************"
cp -r ./target/TrendingApp* /usr/local/tomcat/webapps/
