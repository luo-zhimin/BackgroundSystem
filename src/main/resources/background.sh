#!/bin/sh

WEB_HOME=$(cd $(dirname $0); pwd)

if [ ! -e *.jar ];then
   echo "请将脚本放至项目jar包同级目录中"
   exit 1
fi

FILE_NAME=`ls $WEB_HOME | grep *.jar`

stopprocess(){
   pid=$1
   kill $pid
while [ ! -z $pid  ]
   do
     pid=`ps ax | awk '{ print 1 }' | grep -e ${pid}`
     if [ -z $pid  ] ; then
        echo ""
        echo "STOPPED"
     else
        printf '.'
        sleep 1s
    fi
 done
}

case $1 in
start)
    echo -n "${FILE_NAME} Starting ..."
    if [ -f "pidfile.pid" ];then
	if kill -0 `cat "pidfile.pid"` > /dev/null 2>&1;then
	   echo $command already running as process `cat "pidfile.pid"`.
	   exit 0
        fi
    fi
    nohup java -DBIG_DATA_CONFIG_HOME=$WEB_HOME/config -Djasypt.encryptor.password=5de88f71a509010ba5a0491c751b2d77  -javaagent:BackgroundSystem-0.0.1-SNAPSHOT-encrypted.jar='-pwd backgroundSystem' -jar BackgroundSystem-0.0.1-SNAPSHOT-encrypted.jar  2>&1< /dev/null &
#    nohup java -DBIG_DATA_CONFIG_HOME=$WEB_HOME/config -jar $WEB_HOME/$FILE_NAME  2>&1< /dev/null &
    if [ $? -eq 0 ]
    then
	if /bin/echo -n $! >  pidfile.pid
	then 
	   sleep 1
	   echo START
	else
	   echo FAILED TO WRITE PID
	   exit 1
        fi
    else
	echo SERVER DID NOT START
	exit 1
    fi
    ;;
stop)
    echo -n "${FILE_NAME} Stopping ..."
    if [ ! -f "pidfile.pid" ]
    then
        echo "NO SERVER TO STOP (COULD NOT FIND FILE pidfile.pid)"
    else
        stopprocess $(cat "pidfile.pid")
        rm "pidfile.pid"
    fi
    exit 0
   ;;
restart)
   shift
   "$0" stop ${@}
   sleep 5
   "$0" start ${@}
   ;;
*)
   echo "Usage: $0 {start|stop|restart}" >&2
esac
