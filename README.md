# spring-boot-starter-fastdfs

#### 项目介绍
fastdfs starter for spring boot

#### 集成
1.添加依赖:
```xml
     <dependency>
        <groupId>io.github.whiliang</groupId>
        <artifactId>spring-boot-starter-fastdfs</artifactId>
        <version>1.2.7.RELEASE</version>
     </dependency>
```
2.添加配置信息
```yaml
spring:
  fastdfs:
    enable: true
    max-pool-size: 10
    min-pool-size: 1
    wait-times: 200
    file-proxy-server: http://192.168.1.100:8080/
    tracker-list:
      - 192.168.1.100:22122
```
3.程序调用
```java
@RestController
public class FileApi {

    @Resource
    private FastdfsFileService fastdfsFileService;
  
    @Resource
    private FastdfsProperties fastdfsProperties;
    
    @PostMapping({"/test"})
    public BaseResponse test(MultipartFile file) {
        try {

            //file upload
            String relativeFilePath = this.fastdfsFileService.uploadWithoutGroup(FileCopyUtils.copyToByteArray(file.getInputStream()), FileUtil.getFileExtension(file.getOriginalFilename()));
            logger.info("upload relativeFilePath: {}", relativeFilePath);
        
            String absoluteFileUrl=fastdfsProperties.getFileProxyServer()+relativeFilePath;
            logger.info("upload absoluteFileUrl: {}", absoluteFileUrl);

            //file download
            FileCopyUtils.copy(
                            fastdfsFileService.download(relativeFilePath),
                            new File("/tmp/test."+FileUtil.getFileExtension(file.getOriginalFilename()))
                    );

            //file delete
            fastdfsFileService.delete(relativeFilePath);

            return BaseResponse.returnOk();
        } catch (Exception e) {
            logger.error("测试失败"+e.getMessage(), e);
            return BaseResponse.error(e.getMessage());
        }
    }
}
```

