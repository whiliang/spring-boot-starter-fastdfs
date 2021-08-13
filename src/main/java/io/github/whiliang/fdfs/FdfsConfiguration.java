package io.github.whiliang.fdfs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * FdfsConfiguration
 *
 * @author whiliang
 */
@Configuration
@ConditionalOnClass(FastdfsFileServiceImpl.class)
@EnableConfigurationProperties(FastdfsProperties.class)
@ComponentScan("io.github.whiliang.fdfs")
@ConditionalOnProperty(
        prefix = "spring.fastdfs",
        name = "enable",
        havingValue = "true"
)
public class FdfsConfiguration {

    @Resource
    private FastdfsProperties fastdfsProperties;

    @Bean
    public FastdfsFileServiceImpl fastdfsFileServiceImpl() {
        return new FastdfsFileServiceImpl(fastdfsProperties);
    }
}
