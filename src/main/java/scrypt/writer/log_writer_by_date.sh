#!/bin/bash

# Name: log_writer_by_date.sh
# Enter parameters: $1, $2
#   where: $1 - start value of time interval,
#          $2 - end value of time interval
# Result of execution: write data from system
#   journal to special files, which
#   contains in logFiles directory by types of logs
#

# Назначение входных параметров переменным
# для более удобной работы с параметрами

# Назначение входного параметра переменной startDate
startDate=$1

# Назначение входного параметра переменной endDate
endDate=$2
# '5-16-2024 23:49:59'
#

alsoDate=$(date +'%m-%e-%Y')
# shellcheck disable=SC1073
time=$(date | awk '{print $5}')

if [ "$endDate" -ne "$asloDate" ]
then
  time="23:59:59"
fi

# Прибавляем переменной endDate максимальное время в дне
# для того чтобы охватывать записи по всему крайнему дню
endDate+="$time"

echo $endDate

# Очищение файлов
sh src/main/java/scrypt/clear.sh

# Запись всех типов
journalctl -p 7 --since=$startDate --until=$endDate | grep -E '^[а-я]{3,4} [0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}'> src/main/java/logFiles/allTypesLogs/all_types



