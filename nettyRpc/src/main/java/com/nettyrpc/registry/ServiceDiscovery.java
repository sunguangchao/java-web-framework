package com.nettyrpc.registry;

import com.nettyrpc.client.ConnectManage;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 服务发现
 */
public class ServiceDiscovery {
    private final static Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    private String registryAddress;
    private ZooKeeper zooKeeper;

    public ServiceDiscovery(String registryAddress){
        this.registryAddress = registryAddress;
        //连接zk
        zooKeeper = connectServer();
        if (zooKeeper != null){
            watchNode(zooKeeper);
        }

    }

    /**
     * 连接zk，并监听相关事件
     * @return
     */
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
            //如果不是目的状态，则一直等待
            latch.await();
        }catch (IOException | InterruptedException e){
            logger.error("connectServer Exception", e);
        }
        return zk;
    }


    private void watchNode(final ZooKeeper zk){
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                        watchNode(zk);
                    }
                }
            });
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList){
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            logger.debug("node data:{}", dataList);
            this.dataList = dataList;
            logger.debug("Service discovery triggered updating connected server node.");
            UpdateConnectedServer();
        }catch (KeeperException | InterruptedException e){
            logger.error("watchNode Exception", e);
        }
    }

    private void UpdateConnectedServer(){
        ConnectManage.getInstance().updateConnectedServer(this.dataList);
    }

    public String discover(){
        String data = null;
        int size = dataList.size();
        if (size > 0){
            if (size == 1){
                data = dataList.get(0);
                logger.debug("using only data: {}", data);
            }else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                logger.debug("using random data: {}", data);
            }
        }
        return data;
    }

    public void stop(){
        if (zooKeeper != null){
            try {
                zooKeeper.close();
            }catch (InterruptedException e){
                logger.error("stop exception", e);
            }
        }
    }

}
