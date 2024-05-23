#!/bin/bash

# TODO: check existing file.csv

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

# main function (read logs from file.csv and create json file.csv)
# parameter ${1} - from what file.csv get data
# parameter ${2} - where write json

function main() {
  ind=1

  touch ${2}

  while read -r p;
  do
      if [[ -z  ${p} ]]
      then
          continue
      fi
      createJsonFromLine ${ind} ${p}
      ((ind++))
  done < ${1} | jq -s add > $2
}


main $1 $2



