#!/bin/bash

cp -r ~/step55-2020/src/main/java/com/google/sps/data/. javafiles
cp -r ~/step55-2020/src/main/java/com/google/sps/servlets/. javafiles

javadoc -sourcepath ./javafiles -d ./output -subpackages .