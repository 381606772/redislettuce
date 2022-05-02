启动redis集群：
1、redis安装目录：/usr/local/redis
2、redis配置文件：
    /root/cluster/7001/redis.conf
    /root/cluster/7002/redis.conf
    /root/cluster/7003/redis.conf
    /root/cluster/8001/redis.conf
    /root/cluster/8002/redis.conf
    /root/cluster/8003/redis.conf
3、开启redis集群：
    进入到目录/usr/local/redis, 执行如下命令:
    nohup bin/redis-server /root/cluster/7001/redis.conf &
    nohup bin/redis-server /root/cluster/7002/redis.conf &
    nohup bin/redis-server /root/cluster/7003/redis.conf &
    nohup bin/redis-server /root/cluster/8001/redis.conf &
    nohup bin/redis-server /root/cluster/8002/redis.conf &
    nohup bin/redis-server /root/cluster/8003/redis.conf &
4、ps -ef|grep redis，查看redis是否开启成功.




redis commands reference(redis 命令参考文档): redis.io/commands

