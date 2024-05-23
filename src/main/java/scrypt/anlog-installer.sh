#!/bin/sh


if  ! hash jq
then
    echo "downloading jq..."
    sudo apt-get install jq
fi




touch ~/usr/bin/anlog;

cd ~/usr/bin;

echo -e "#!/bin/sh\n\n\n
function json-conv() {\n
    pltype=${6:0:(${#6} - 1)}
    plmess=${@:7}
    jq -n \
    --arg key "log-$1" \
    --argjson date "$(jq -n \
   --arg month "$2" \
   --arg day "$3" \
   --arg time "$4" \
   '{$day, $month, $time}')"
    --arg type "$pltype" \
    --arg message "$plmess" \
    '{($key): {$date, $type, $message}}'
}\n\n


#TODO: implement
function get-json(){\n
    file_name=$1
}


#TODO: implement
function get-logs() {
}


func=${1}
shift;

if ! declare -f $func > dev/null
then
    "function ${func} not found";
else
    func $@;
fi

" >> anlog