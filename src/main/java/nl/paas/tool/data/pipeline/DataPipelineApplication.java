package nl.paas.tool.data.pipeline;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableFeignClients
@EnableOpenApi
@MapperScan(basePackages = "nl.paas.tool.data.pipeline")
public class DataPipelineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPipelineApplication.class, args);
    }

}
