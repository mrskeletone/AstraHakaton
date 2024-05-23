#!/bin/sh

# Download jq (json convertor) if it doesn't exist on local machine
if  ! hash jq
then
    echo "downloading jq..."
    sudo apt-get install jq
fi

# Create anlog file
sudo touch /usr/bin/anlog;

# Allow execute command
sudo chmod +x /usr/bin/anlog

# Add script in anlog file
echo -e "
#!/bin/sh


# TODO: check existing file

# function creating json-object of single log
function createJsonFromLine() {
  pl=\${6:0:(\${#6} - 1)}
  payload=\${@:7}

  jq -n \
  --argjson date \"\$(jq -n \\
    --arg month \"\$2\" \\
    --arg day \"\$3\" \\
    --arg time \"\$4\" \\
    '{\$day, \$month, \$time}')\" \\
  --arg type \"\$pl\" \\
  --arg message \"\$payload\" \\
	--arg key \"log-\$1\" \\
	'{(\$key): {\$date, \$type, \$message}}'
}


#main function (read logs from file and create json file)
# parameter \${1} - from what file get data
# parameter \${2} - where write json
# next parameters needs for filter logs by type
function jsonc() {
  ind=1
  from=\${1}
  to=\${2}
  touch \$to
  greping=\"^(\${3}\"
  shift 3
  for var in \$@
  do
    greping+=\"|\$var\"
  done
  greping+=\")\"
  while read -ra p;
  do
    is=\$(echo \${p[4]} | grep -Ec \$greping)
    if [[ -z  \${p} || \$is -eq 0 ]]
    then
      continue
    fi
    createJsonFromLine \${ind} \${p[@]}
    ((ind++))
  done < \$from | jq -s add > \${to}
}



# TODO: check existing file
function csvc() {
  # TODO: implement flags
  while getopts \":h\" option
   do
     case \$option in
        h)
           echo \"this is help command csvc\"
           exit;;
     esac
  done
  from=\$1
  to=\$2

  awk 'BEGIN{print \"ID,Month,Day,Time,Type\"}' > \${to}
  ind=1
  while read -ra p;
  do
    if [[ -z \${p} ]]
    then
      continue
    fi
    echo \${ind} \${p[4]:0:(\${#p[4]} - 1)} \${p[@]} | awk '{print \$1\",\" \$3\",\" \$4\",\" \$5\",\" \$2}';
    ((ind++))
  done < \${from} >> \${to}
}




# default write logs in current folder with default name <number of log>.txt
function getlogs() {
  if [[ \"\${1}\" == \"-h\" || \"\${1}\" == \"--help\" ]]
  then
    echo \"text help from getlogs\"
    exit
    # TODO: implement
  fi
  type=7
  name=\"\"
  all=false
  while getopts \":a:t:n:\" option
  do
    case \$option in
      a)
        all=true
        ;;
      t)
        type=\$OPTARG
        ;;
      n)
        name=\$OPTARG
        ;;
    esac
  done

  if [ \${name} -z ]
  then
    name=\"log\${type}.txt\"
  fi

  touch \$name

  if [[ \$all || \$type == 0 ]]
  then
    journalctl -p \$type | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}' > name
    exit 0
  fi

  awk 'FNR==NR{a[\$0]++;next}(!(\$0 in a))' journalctl -p \${type} - 1 journalctl -p \$type | \
  grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}' > \$name
}




func=\${1}
shift;

if [[ \"\${func}\" == \"-h\" || \"\${func}\" == \"--help\" ]]
then
  echo \"this is help command\" # TODO: write help text
  exit 0
elif [[ \"\$func\" == \"-v\" || \"\$func\" == \"--version\" ]]
then
  echo \"Anlog version is the best\"
  exit 0
elif ! declare -f \${func} > /dev/null
then
  echo \"Function \${func} not found.\"
  exit 0
else
  \$func \$@;
fi
" | sudo tee /usr/bin/anlog



# ---add user to systemd-journal with sudo start

sudo usermod -a -G systemd-journal $USER
exec su -l $USER
# If you want delete user from this group use command "gpasswd --delete <username> <groupname>"

# ---add user to systemd-journal with sudo end
