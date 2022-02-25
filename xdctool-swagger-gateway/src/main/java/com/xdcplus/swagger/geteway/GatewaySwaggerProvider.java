package com.xdcplus.swagger.geteway;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合其他微服务的api资源
 *
 * @author Rong.Jia
 * @date 2021/05/11 13:33
 */
public class GatewaySwaggerProvider implements SwaggerResourcesProvider {

    private RouteLocator routeLocator;
    private GatewayProperties gatewayProperties;
    private SwaggerGatewayProperties swaggerGatewayProperties;

    public GatewaySwaggerProvider(RouteLocator routeLocator,
                                  GatewayProperties gatewayProperties,
                                  SwaggerGatewayProperties swaggerGatewayProperties) {
        this.routeLocator = routeLocator;
        this.swaggerGatewayProperties = swaggerGatewayProperties;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();

        //取出gateway的route
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));

        //结合配置的route-路径(Path)，和route过滤，只获取有效的route节点
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> resources.add(SwaggerUtils.swaggerResource(routeDefinition.getId(),
                                predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                        .replace("/**", swaggerGatewayProperties.getSwaggerApiDocs()),
                                swaggerGatewayProperties.getSwaggerVersion()))));
        return resources;
    }






}