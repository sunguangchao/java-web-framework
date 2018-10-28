package com.nettyrpc.registry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 服务注册
 * zk集群上注册服务地址
 */
public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress){
        this.registryAddress = registryAddress;
    }

    public void register(String date){
        if (date != null){
            ZooKeeper zk = connectServer();
            if (zk != null){
                //add ROOT node if not exist
                addRootNode(zk);
                createNode(zk, date);
            }
        }
    }

    private ZooKeeper connectServer(){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
                        latch.countDown();
                    }
                }
            });
            latch.await();
        }catch (IOException e){
            logger.error(e.getMessage());
        }catch (InterruptedException e){
            logger.error(e.getMessage());
        }
        return zk;
    }

    private void addRootNode(ZooKeeper zk){
        try {
            Stat s = zk.exists(Constant.ZK_REGISTRY_PATH, false);
            if (s == null){
                /**
                 * https://www.cnblogs.com/shengkejava/p/5611671.html
                 * 同步创建
                 * ZooDefs.Ids.OPEN_ACL_UNSAFE:完全开放
                 * CreateMode.PERSISTENT:持久化节点
                 */
                zk.create(Constant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }catch (KeeperException e){
            logger.error(e.getMessage());
        }catch (InterruptedException ex){
            logger.error(ex.getMessage());
        }
    }

    /**
     * 创建节点
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data){
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create zookeeper node ({} => {})", path, data);
        }catch (KeeperException e){
            logger.error(e.getMessage());
        }catch (InterruptedException ex){
            logger.error(ex.getMessage());
        }

    }

}
