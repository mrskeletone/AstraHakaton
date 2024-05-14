#!/bin/bash

#Создаём буффер для логов и выводим сообщение о его создании
touch ../logFiles/buffer

#Очищение файлов
echo -n > ../logFiles/emergency
echo -n > ../logFiles/alerts
echo -n > ../logFiles/critical
echo -n > ../logFiles/errors
echo -n > ../logFiles/warning
echo -n > ../logFiles/notice
echo -n > ../logFiles/info
echo -n > ../logFiles/debug

#Заполнение файлов уникальными элементами (0-7)
journalctl -p 0 > ../logFiles/emergency

journalctl -p 1 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/emergency |sort |uniq -u >> ../logFiles/alerts
echo -n > ../logFiles/buffer

journalctl -p 2 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/alerts |sort |uniq -u >> ../logFiles/critical
echo -n > ../logFiles/buffer

journalctl -p 3 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/critical |sort |uniq -u >> ../logFiles/errors
echo -n > ../logFiles/buffer

journalctl -p 4 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/errors |sort |uniq -u >> ../logFiles/warning
echo -n > ../logFiles/buffer

journalctl -p 5 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/warning |sort |uniq -u >> ../logFiles/notice
echo -n > ../logFiles/buffer
journalctl -p 6 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/notice |sort |uniq -u >> ../logFiles/info
echo -n > ../logFiles/buffer

journalctl -p 7 > ../logFiles/buffer
cat ../logFiles/buffer ../logFiles/info |sort |uniq -u >> ../logFiles/debug

rm ../logFiles/buffer

