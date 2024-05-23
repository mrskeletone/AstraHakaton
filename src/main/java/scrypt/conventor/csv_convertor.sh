#!/bin/bash

# TODO: check existing file
function main() {
  from=$1
  to=$2
  greping="^(${3}"
  shift 3
  for var in $@
  do
    greping+="|$var"
  done
  greping+=")"
  awk 'BEGIN{print "ID,Month,Day,Time,Type"}' > ${to}
  ind=1
  echo $greping
  while read -ra p;
  do
    is=$(echo ${p[4]} | grep -Ec $greping)
    if [[ -z ${p} || $is -eq 0 ]]
    then
      continue
    fi
    echo ${ind} ${p[4]:0:(${#p[4]} - 1)} ${p[@]} | awk '{print $1"," $3"," $4"," $5"," $2}';
    ((ind++))
  done < ${from} >> ${to}
}

main $@