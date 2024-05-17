#!/bin/bash

# Переменные задающие временной промежуток

# 2024-04-04
# 2024-04-08 23:59:59

startDate=$1
endDate=$2
time="23:59:59"

endDate+="$time"

#Создаём буффер для логов и выводим сообщение о его создании
touch src/main/java/logFiles/buffer

#Очищение файлов
sh src/main/java/scrypt/clear.sh

# Запись всех типов
journalctl -p 7 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/allTypesLogs/all_types

## Заполнение файлов уникальными элементами (0-6)
  #Запись emergency
journalctl -p 0 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/emergencyLogs/emergency

  #Запись alerts
journalctl -p 1 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/emergencyLogs/emergency |sort |uniq -u >> src/main/java/logFiles/alertsLogs/alerts
echo -n > src/main/java/logFiles/buffer

  #Запись critical
journalctl -p 2 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/alertsLogs/alerts |sort |uniq -u >> src/main/java/logFiles/criticalLogs/critical
echo -n > src/main/java/logFiles/buffer

  #Запись errors
journalctl -p 3 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/criticalLogs/critical |sort |uniq -u >> src/main/java/logFiles/errorLogs/errors
echo -n > src/main/java/logFiles/buffer

  #Запись warning
journalctl -p 4 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/errorLogs/errors |sort |uniq -u >> src/main/java/logFiles/warningLogs/warning
echo -n > src/main/java/logFiles/buffer

  #Запись notice
journalctl -p 5 --since=$startDate --until=$endDate |grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/warningLogs/warning |sort |uniq -u >> src/main/java/logFiles/noticeLogs/notice
echo -n > src/main/java/logFiles/buffer

  #Запись info
journalctl -p 6 --since=$startDate --until=$endDate |grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/noticeLogs/notice |sort |uniq -u >> src/main/java/logFiles/infoLogs/info
echo -n > src/main/java/logFiles/buffer

  #Запись debug
journalctl -p 7 --since=$startDate --until=$endDate |grep -E '^[а-я]{3,4}\ [0-9]{1,2}\ [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/infoLogs/info |sort |uniq -u >> src/main/java/logFiles/debugLogs/debug
rm src/main/java/logFiles/buffer
