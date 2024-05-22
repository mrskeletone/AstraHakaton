#!/bin/bash

# TODO: check existing file
function main() {
  from=$1
  to=$2

  awk 'BEGIN{print "ID,Month,Day,Time,Type"}' > ${to}
  ind=1
  while read -r p;
  do
    if [[ -z ${p} ]]
    then
      continue
    fi
    echo $ind ${p} | awk '{print $1"," $2"," $3"," $4"," $6}'
    ((ind++))
  done < ${from} >> ${to}
}

main $1 $2
