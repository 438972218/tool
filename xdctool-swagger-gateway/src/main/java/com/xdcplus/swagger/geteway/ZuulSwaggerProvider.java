package com.xdcplus.swagger.geteway;

import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 聚合其他微服务的api资源
 *
 * @author Rong.Jia
 * @date 2021/05/11 13:33
 */
public class ZuulSwaggerProvider implements SwaggerResourcesProvider {

    private RouteLocator routeLocator;
    private ZuulProperties zuulProperties;
    private SwaggerGatewayProperties swaggerGatewayProperties;

    public ZuulSwaggerProvider(RouteLocator routeLocator,
                               ZuulProperties zuulProperties,
                                  SwaggerGatewayProperties swaggerGatewayProperties) {
        this.routeLocator = routeLocator;
        this.swaggerGatewayProperties = swaggerGatewayProperties;
        this.zuulProperties = zuulProperties;
    }

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //取出gateway的route
        routeLocator.getRoutes().forEach(route -> routes.add(route.getId()));

        Map<String, ZuulProperties.ZuulRoute> zuulRouteMap = zuulProperties.getRoutes();
        zuulRouteMap.entrySet().stream()
                .filter(zuulRoute -> routes.contains(zuulRoute.getValue().getId()))
                .forEach(zuulRoute -> resources.add(SwaggerUtils.swaggerResource(zuulRoute.getKey(),
                        zuulRoute.getValue().getPath().replace("/**", swaggerGatewayProperties.getSwaggerApiDocs()),
                        swaggerGatewayProperties.getSwaggerVersion())));
        return resources;
    }













}