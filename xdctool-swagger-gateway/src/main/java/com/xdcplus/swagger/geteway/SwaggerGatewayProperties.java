package com.xdcplus.swagger.geteway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * Swagger 网关聚合属性
 * @author Rong.Jia
 * @date 2021/07/12
 */
@Data
@ConfigurationProperties(prefix = "xdc.swagger.gateway")
public class SwaggerGatewayProperties {

    /**
     * swagger interface version default:  3.0
     */
    private String swaggerVersion = "3.0";

    /**
     * swagger api-docs default: /v3/api-docs
     */
    private String swaggerApiDocs = "/v3/api-docs";

    /**
     *  header Name， 默认： X-Forwarded-Prefix
     */
    private String headerName = "X-Forwarded-Prefix";



}
