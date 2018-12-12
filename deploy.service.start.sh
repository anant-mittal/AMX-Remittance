#!/bin/bash

ps_out=`ps -ef | grep $1 | grep -v 'grep' | grep -v $0`
result=$(echo $ps_out | grep "$1")

if [[ ! -f /var/run/$2/$2.pid ]]
then
    	echo  "Service is NOT FOUND and will start now"
        mkdir -p /home/amxdev/jax/$2
        rm /etc/init.d/$2
        ln -s /home/amxdev/jax/$2/$1-0.0.1-SNAPSHOT.jar /etc/init.d/$2
        /etc/init.d/$2 restart
        echo  "Service has started"
elif [[ "$result" != "" ]]
then
    	echo "Service will Restart"
        kill -9 $(cat /var/run/$2/$2.pid)
        /etc/init.d/$2 restart
else
    	echo  "Service is NOT running and will start now"
        mkdir -p /home/amxdev/jax/$2
        rm /etc/init.d/$2
        ln -s /home/amxdev/jax/$2/$1-0.0.1-SNAPSHOT.jar /etc/init.d/$2
        /etc/init.d/$2 restart
        echo  "Service has started"
fi

(tail -f /var/log/$2.log & P=$! ; sleep 60; kill -9 $P)