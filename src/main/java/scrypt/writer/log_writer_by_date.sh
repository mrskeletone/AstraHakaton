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
echo -n > src/main/java/logFiles/emergency
echo -n > src/main/java/logFiles/alerts
echo -n > src/main/java/logFiles/critical
echo -n > src/main/java/logFiles/errors
echo -n > src/main/java/logFiles/warning
echo -n > src/main/java/logFiles/notice
echo -n > src/main/java/logFiles/info
echo -n > src/main/java/logFiles/debug

journalctl -p 7 --since=$startDate --until=$endDate> src/main/java/logFiles/all_types

#Заполнение файлов уникальными элементами (0-6)
journalctl -p 0 --since=$startDate --until=$endDate> src/main/java/logFiles/emergency

journalctl -p 1 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/emergency |sort |uniq -u >> src/main/java/logFiles/alerts
echo -n > src/main/java/logFiles/buffer

journalctl -p 2 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/alerts |sort |uniq -u >> src/main/java/logFiles/critical
echo -n > src/main/java/logFiles/buffer

journalctl -p 3 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/critical |sort |uniq -u >> src/main/java/logFiles/errors
echo -n > src/main/java/logFiles/buffer

journalctl -p 4 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/errors |sort |uniq -u >> src/main/java/logFiles/warning
echo -n > src/main/java/logFiles/buffer

journalctl -p 5 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/warning |sort |uniq -u >> src/main/java/logFiles/notice
echo -n > src/main/java/logFiles/buffer
journalctl -p 6 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/notice |sort |uniq -u >> src/main/java/logFiles/info
echo -n > src/main/java/logFiles/buffer

journalctl -p 7 --since=$startDate --until=$endDate> src/main/java/logFiles/buffer
cat src/main/java/logFiles/buffer src/main/java/logFiles/info |sort |uniq -u >> src/main/java/logFiles/debug
rm src/main/java/logFiles/buffer
