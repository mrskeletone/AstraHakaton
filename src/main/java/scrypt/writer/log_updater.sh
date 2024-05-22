#!/bin/bash

# if $1 = curDate
# last string of all_types date
# lastTime -> curTime

startDate=$(date +'%Y-%m-%e')
startTime=$1
endTime=$(date|awk '{print $5}')
endDate=$(date +'%Y-%m-%e')
startDate+="$startTime"
endDate+="$endTime"

echo $startDate
echo $endDate

journalctl -p 7 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}' >> src/main/java/logFiles/allTypesLogs/all_types

touch src/main/java/logFiles/buffer

journalctl -p 0 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/emergencyLogs/emergency

  # Запись alerts
journalctl -p 1 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/emergencyLogs/emergency |sort |uniq -u >> src/main/java/logFiles/alertsLogs/alerts
echo -n > src/main/java/logFiles/buffer

  # Запись critical
journalctl -p 2 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/alertsLogs/alerts |sort |uniq -u >> src/main/java/logFiles/criticalLogs/critical
echo -n > src/main/java/logFiles/buffer

  # Запись errors
journalctl -p 3 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/criticalLogs/critical |sort |uniq -u >> src/main/java/logFiles/errorLogs/errors
echo -n > src/main/java/logFiles/buffer

  # Запись warning
journalctl -p 4 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/errorLogs/errors |sort |uniq -u >> src/main/java/logFiles/warningLogs/warning
echo -n > src/main/java/logFiles/buffer

  # Запись notice
journalctl -p 5 --since=$startDate --until=$endDate |grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/warningLogs/warning |sort |uniq -u >> src/main/java/logFiles/noticeLogs/notice
echo -n > src/main/java/logFiles/buffer

  # Запись info
journalctl -p 6 --since=$startDate --until=$endDate |grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/noticeLogs/notice |sort |uniq -u >> src/main/java/logFiles/infoLogs/info
echo -n > src/main/java/logFiles/buffer

  # Запись debug
journalctl -p 7 --since=$startDate --until=$endDate |grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/infoLogs/info |sort |uniq -u >> src/main/java/logFiles/debugLogs/debug

rm src/main/java/logFiles/buffer