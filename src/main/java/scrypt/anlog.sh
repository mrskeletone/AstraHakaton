#!/bin/sh


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


#main function (read logs from file and create json file)
# parameter ${1} - from what file get data
# parameter ${2} - where write json
# next parameters needs for filter logs by type
function jsonc() {
  ind=1
  from=${1}
  to=${2}
  touch $to
  greping="^(${3}"
  shift 3
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



# TODO: check existing file
function csvc() {
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




# default write logs in current folder with default name <number of log>.txt
function getlogs() {
  if [[ "${1}" == "-h" || "${1}" == "--help" ]]
  then
    echo "text help from getlogs"
    exit
    # TODO: implement
  fi
  type=7
  name=""
  all=false
  while getopts ":a:t:n:" option
  do
    case $option in
      a)
        all=true
        ;;
      t)
        type=$OPTARG
        ;;
      n)
        name=$OPTARG
        ;;
    esac
  done

  if [ ${name} -z ]
  then
    name="log${type}.txt"
  fi

  touch $name

  if [[ $all || $type == 0 ]]
  then
    journalctl -p $type | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}' > $name
    exit 0
  fi

  awk 'FNR==NR{a[$0]++;next}(!($0 in a))' journalctl -p ${type} - 1 journalctl -p $type \
  | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}' > $name
}




func=${1}
shift;

#if [[ -z ${func} ]]
#then
#    echo "Enter function name"
#    exit 0
if [[ "${func}" == "-h" || "${func}" == "--help" ]]
then
  echo "this is help command" # TODO: write help text
  exit 0
elif [[ "$func" == "-v" || "$func" == "--version" ]]
then
  echo "Anlog version is the best"
  exit 0
elif ! declare -f ${func} > /dev/null
then
  echo "Function ${func} not found."
  exit 0
else
  $func $@;
fi