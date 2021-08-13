package io.github.whiliang.fdfs;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 文件服务
 *
 * @author whiliang
 */
public class FastdfsFileServiceImpl implements FastdfsFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastdfsFileServiceImpl.class);
    /**
     * 连接池
     */
    private FdfsConnectionPool connectionPool = null;

    /**
     * fastdfs配置
     */
    private FastdfsProperties fastdfsProperties;

    public FastdfsFileServiceImpl(FastdfsProperties fdfsProperties) {
        this.fastdfsProperties = fdfsProperties;
        this.initConnectionPool();
    }

    /**
     * 初始化线程池
     *
     * @Description:
     */
    public void initConnectionPool() {
        String logId = UUID.randomUUID().toString();
        LOGGER.info("[初始化线程池(Init)][" + logId + "][默认参数：minPoolSize="
                + fastdfsProperties.getMinPoolSize() + ",maxPoolSize=" + fastdfsProperties.getMaxPoolSize() + ",waitTimes="
                + fastdfsProperties.getWaitTimes() + "]");
        this.connectionPool = new FdfsConnectionPool(fastdfsProperties.getTrackerList().stream()
                .collect(Collectors.joining(",")), fastdfsProperties.getMinPoolSize(), fastdfsProperties.getMaxPoolSize(), fastdfsProperties.getWaitTimes());
    }

    @Override
    public String uploadWithoutGroup(byte[] fileBytes, String extName) throws FastdfsException {
        String logId = UUID.randomUUID().toString();
        /** 封装文件信息参数 */
        NameValuePair[] metaList = new NameValuePair[]{new NameValuePair(
                "fileName", "")};
        TrackerServer trackerServer = null;
        try {

            /** 获取fastdfs服务器连接 */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);

            /** 以文件字节的方式上传 */
            String[] results = client1.upload_file(fileBytes,
                    extName, metaList);

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            /** results[0]:组名，results[1]:远程文件名 */
            if (results != null && results.length == 2) {
                return results[0] + "/" + results[1];
            } else {
                /** 文件系统上传返回结果错误 */
                throw ERRORS.UPLOAD_RESULT_ERROR.ERROR();
            }
        } catch (FastdfsException e) {

            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            throw e;

        } catch (SocketTimeoutException e) {
            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            throw ERRORS.WAIT_IDLECONNECTION_TIMEOUT.ERROR();
        } catch (Exception e) {

            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            connectionPool.drop(trackerServer, logId);
            throw ERRORS.SYS_ERROR.ERROR();

        }

    }


    @Override
    public String uploadWithGroup(byte[] fileBytes, String group, String extName) throws FastdfsException {
        String logId = UUID.randomUUID().toString();
        /** 封装文件信息参数 */
        NameValuePair[] metaList = new NameValuePair[]{new NameValuePair(
                "fileName", "")};
        TrackerServer trackerServer = null;
        try {

            /** 获取fastdfs服务器连接 */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);

            /** 以文件字节的方式上传 */
            String[] results = client1.upload_file(group, fileBytes,
                    extName, metaList);

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            /** results[0]:组名，results[1]:远程文件名 */
            if (results != null && results.length == 2) {
                return results[0] + "/" + results[1];
            } else {
                /** 文件系统上传返回结果错误 */
                throw ERRORS.UPLOAD_RESULT_ERROR.ERROR();
            }
        } catch (FastdfsException e) {

            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            throw e;

        } catch (SocketTimeoutException e) {
            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            throw ERRORS.WAIT_IDLECONNECTION_TIMEOUT.ERROR();
        } catch (Exception e) {

            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            connectionPool.drop(trackerServer, logId);
            throw ERRORS.SYS_ERROR.ERROR();

        }

    }


    /**
     * @param remote_filename 远程文件名称
     * @throws FastdfsException
     * @Description: 删除fastdfs服务器中文件
     */
    @Override
    public void delete(String remote_filename) throws FastdfsException {

        String logId = UUID.randomUUID().toString();
        LOGGER.info("[ 删除文件（deleteFile）][" + logId + "][parms：remote_filename=" + remote_filename + "]");
        TrackerServer trackerServer = null;

        try {
            /** 获取可用的tracker,并创建存储server */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 client1 = new StorageClient1(trackerServer,
                    storageServer);
            /** 删除文件,并释放 trackerServer */
            int result = client1.delete_file1(remote_filename);

            /** 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            LOGGER.info("[ 删除文件（deleteFile）--调用fastdfs客户端返回结果][" + logId
                    + "][results：result=" + result + "]");

            /** 0:文件删除成功，2：文件不存在 ，其它：文件删除出错 */
            if (result == 2) {
                throw ERRORS.NOT_EXIST_FILE.ERROR();
            } else if (result != 0) {
                throw ERRORS.DELETE_RESULT_ERROR.ERROR();
            }
        } catch (FastdfsException | MyException | IOException e) {
            LOGGER.error("[ 删除文件（deleteFile）][" + logId + "][异常：" + e + "]");
        }
    }


    @Override
    public byte[] download(String fileId) throws FastdfsException {
        Assert.notNull(fileId, "File id must not be null.");
        String logId = UUID.randomUUID().toString();
        TrackerServer trackerServer = null;
        /** 获取fastdfs服务器连接 */
        trackerServer = connectionPool.checkout(logId);
        StorageServer storageServer = null;
        StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);

        byte[] file = null;
        try {
            file = client1.download_file1(fileId);
        } catch (IOException | MyException e) {
            LOGGER.error("Error while downloading file.", e);
            throw new FastdfsException("Error while downloading file.", e.getMessage());
        }
        /** 上传完毕及时释放连接 */
        connectionPool.checkin(trackerServer, logId);

        if (file == null) {
            throw new FastdfsException(" Failed to download file : "
                    , String.valueOf(client1.getErrorCode()));
        }

        return file;
    }
}
