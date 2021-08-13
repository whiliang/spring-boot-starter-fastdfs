package io.github.whiliang.fdfs;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * FastdfsProperties
 *
 * @author whiliang
 */
@ConfigurationProperties("spring.fastdfs")
public class FastdfsProperties {

    /**
     * enable
     */
    private boolean enable;
    /**
     * 连接池默认最小连接数
     */
    private long minPoolSize = 10;
    /**
     * 连接池默认最大连接数
     */
    private long maxPoolSize = 30;
    /**
     * 当前创建的连接数
     */
    private volatile long nowPoolSize = 0;
    /**
     * 默认等待时间（单位：秒）
     */
    private long waitTimes = 200;

    /**
     * 集群地址
     * http:port
     */
    private List<String> trackerList;

    /**
     * 文件服务器域名，eg: http://192.168.1.100:8080/
     */
    private String fileProxyServer;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(long minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public long getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(long maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getNowPoolSize() {
        return nowPoolSize;
    }

    public void setNowPoolSize(long nowPoolSize) {
        this.nowPoolSize = nowPoolSize;
    }

    public long getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(long waitTimes) {
        this.waitTimes = waitTimes;
    }

    public List<String> getTrackerList() {
        return trackerList;
    }

    public void setTrackerList(List<String> trackerList) {
        this.trackerList = trackerList;
    }

    public String getFileProxyServer() {
        return fileProxyServer;
    }

    public void setFileProxyServer(String fileProxyServer) {
        this.fileProxyServer = fileProxyServer;
    }
}
