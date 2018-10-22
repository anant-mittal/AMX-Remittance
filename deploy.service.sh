ZSD_USER=$1
ZSD_HOST=$2
ZSD_PASS=$3
ZSD_MODULE=$4
ZSD_NUM=$5
echo  "MAKIING BUILD"
mvn clean package -pl $ZSD_MODULE -am -DskipTests
echo  "MAKING DIR ON REMOTE"
sshpass -p $ZSD_PASS ssh -o StrictHostKeyChecking=no $ZSD_USER@$ZSD_HOST "mkdir -p ~/jax/${ZSD_MODULE}${ZSD_NUM}"
echo  "COPYING FILE ON REMOTE : ${ZSD_MODULE}/target/${ZSD_MODULE}-0.0.1-SNAPSHOT.jar"
sshpass -p $ZSD_PASS scp ${ZSD_MODULE}/target/${ZSD_MODULE}-0.0.1-SNAPSHOT.jar $ZSD_USER@$SD_HOST:~/jax/${ZSD_MODULE}${ZSD_NUM}
echo  "RESTARTING SERVICE"
sshpass -p $ZSD_PASS ssh -o StrictHostKeyChecking=no $ZSD_USER@$ZSD_HOST "bash ~/jax/service.start.sh ${ZSD_MODULE} ${ZSD_MODULE}${ZSD_NUM}"
