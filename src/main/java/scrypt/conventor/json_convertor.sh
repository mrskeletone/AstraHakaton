#!/bin/bash

# TODO: check existing file

# function creating json-object of single log
function createJsonFromLine() {
  pl=${6:0:(${#6} - 1)}
  payload=${@:7}

  jq -n \
  --argjson date "$(jq -n \
    --arg month "$2" \
    --arg day "$3" \
    --arg time "$4" \
    '{$day, $month, $time}')" \
  --arg type "$pl" \
  --arg message "$payload" \
	--arg key "log-$1" \
	'{($key): {$date, $type, $message}}'
}


# main function (read logs from file and create json file)
# parameter ${1} - from what file get data
# parameter ${2} - where write json
function main() {
  ind=1
  from=${1}
  to=${2}
  shift 2
  touch $to
  greping="^(${1}"
  shift
  for var in $@
  do
    greping+="|$var"
  done
  greping+=")"
  while read -ra p;
  do
    is=$(echo ${p[4]} | grep -Ec $greping)
    if [[ -z  ${p} || $is -eq 0 ]]
    then
      continue
    fi
    createJsonFromLine ${ind} ${p[@]}
    ((ind++))
  done < $from | jq -s add > ${to}
}


main $@



