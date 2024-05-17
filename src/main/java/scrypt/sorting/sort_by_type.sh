#!/bin/bash

par=$1
hp=$2

grep ${par} ../../logFiles/${hp} > list

nVar=$(wc -l list | awk '{print $1}')

echo $nVar
