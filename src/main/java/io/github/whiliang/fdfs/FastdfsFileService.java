package io.github.whiliang.fdfs;

/**
 * 文件服务
 *
 * @author whiliang
 */
public interface FastdfsFileService {

    /**
     * 上传文件到FastDFS指定group
     *
     * @param file    文件字节流
     * @param group   组名
     * @param extName 后缀名
     * @return
     * @throws FastdfsException
     */
    String uploadWithGroup(byte[] file, String group, String extName) throws FastdfsException;

    /**
     * 不指定group上传文件到FastDFS
     *
     * @param fileBytes 文件字节流
     * @param extName   后缀名
     * @return
     * @throws FastdfsException
     */
    String uploadWithoutGroup(byte[] fileBytes, String extName) throws FastdfsException;

    /**
     * 文件下载
     *
     * @param fileId 文件ID
     * @return 字节流
     * @throws FastdfsException
     */
    byte[] download(String fileId) throws FastdfsException;

    /**
     * 删除FastDFS指定的文件
     *
     * @param fileId 文件ID
     * @throws FastdfsException
     */
    void delete(String fileId) throws FastdfsException;


}
