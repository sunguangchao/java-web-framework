package com.nettyrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class ConnectManage {
    private static final Logger logger = LoggerFactory.getLogger(ConnectManage.class);
    private volatile static ConnectManage connectManage;

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
    /**
     * 读多写少
     */
    private CopyOnWriteArrayList<RpcClientHandler> connectedHandlers = new CopyOnWriteArrayList<>();
    private Map<InetSocketAddress, RpcClientHandler> connectedServerNodes = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    private long connectTimeoutMillis = 6000L;
    private AtomicInteger roundRobin = new AtomicInteger(0);
    private volatile boolean isRunning = true;

    private ConnectManage(){

    }

    /**
     * 双重检验锁的单例模式
     * @return
     */
    public static ConnectManage getInstance(){
        if (connectManage == null){
            synchronized (ConnectManage.class){
                if (connectManage == null){
                    connectManage = new ConnectManage();
                }
            }
        }
        return connectManage;
    }

    public void updateConnectedServer(List<String> allServerAddress){
        if (allServerAddress != null){
            if (allServerAddress.size()> 0){
                HashSet<InetSocketAddress> newAllServerNodeSet = new HashSet<>();
                //update local serverNodes cache
                //将list转为InetSocketAddress的set形式
                for (int i=0; i < newAllServerNodeSet.size(); ++i){
                    String[] array = allServerAddress.get(i).split(":");
                    if (array.length == 2){
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);
                        final InetSocketAddress remotePeer = new InetSocketAddress(host, port);
                        newAllServerNodeSet.add(remotePeer);
                    }
                }
                // Add new server node
                for (final InetSocketAddress serverNodeAddress : newAllServerNodeSet){
                    if (!connectedServerNodes.keySet().contains(serverNodeAddress)){
                        connectServerNode(serverNodeAddress);
                    }
                }
                // Close and remove invalid server nodes
                for (int i=0; i < connectedHandlers.size(); ++i){
                    RpcClientHandler connectedServerHandler = connectedHandlers.get(i);
                    SocketAddress remotePeer = connectedServerHandler.getRemotePeer();
                    if (!newAllServerNodeSet.contains(remotePeer)){
                        logger.info("Remove invalid remote node:{}", remotePeer);
                        RpcClientHandler handler = connectedServerNodes.get(remotePeer);
                        if (handler != null){
                            handler.close();
                        }
                        connectedServerNodes.remove(remotePeer);
                        connectedHandlers.remove(connectedServerHandler);
                    }
                }
            } else {
                logger.error("No available server node. All server nodes are down !!!");
                for (final RpcClientHandler connectedServerHandler : connectedHandlers){
                    SocketAddress remotePeer = connectedServerHandler.getRemotePeer();
                    RpcClientHandler handler = connectedServerNodes.get(remotePeer);
                    handler.close();
                    connectedServerNodes.remove(connectedServerHandler);
                }
                connectedHandlers.clear();
            }
        }

    }

    public void reconnect(final RpcClientHandler handler, final SocketAddress remotePeer){
        if (handler != null){
            connectedHandlers.remove(remotePeer);
            connectedServerNodes.remove(remotePeer);
        }
        connectServerNode((InetSocketAddress) remotePeer);
    }

    /**
     * 新增server节点
     * @param remotePeer
     */
    private void connectServerNode(final InetSocketAddress remotePeer){
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Bootstrap b = new Bootstrap();
                b.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new RpcClientInitializer());
                ChannelFuture channelFuture = b.connect(remotePeer);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()){
                            logger.debug("Successfully connect to remote server. remote peer = " + remotePeer);
                            RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                            addHandler(handler);
                        }
                    }
                });
            }
        });
    }

    private void addHandler(RpcClientHandler handler){
        connectedHandlers.add(handler);
        InetSocketAddress remoteAddress = (InetSocketAddress) handler.getChannel().remoteAddress();
        connectedServerNodes.put(remoteAddress, handler);
        signalAvailableHandler();
    }


    public RpcClientHandler chooseHandler(){
        int size = connectedHandlers.size();
        while (isRunning && size <= 0){
            try {
                boolean available = waitingForHandler();
                if (available){
                    size = connectedHandlers.size();
                }
            }catch (InterruptedException e){
                logger.error("Waiting for available node is interrupted! ", e);
                throw new RuntimeException("Can't connect any servers!", e);
            }
        }
        int index = (roundRobin.getAndAdd(1) + size)%size;
        return connectedHandlers.get(index);
    }
    private boolean waitingForHandler() throws InterruptedException{
        lock.lock();
        try {
            return connected.await(this.connectTimeoutMillis, TimeUnit.MILLISECONDS);
        }finally {
            lock.unlock();
        }
    }

    public void stop(){
        isRunning = false;
        for (int i = 0; i < connectedHandlers.size(); ++i){
            RpcClientHandler connectServerHandler = connectedHandlers.get(i);
            connectServerHandler.close();
        }
        signalAvailableHandler();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    private void signalAvailableHandler(){
        try {
            connected.signalAll();
        }finally {
            lock.unlock();
        }
    }
}
