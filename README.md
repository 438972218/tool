# xdctool 核心工具包

## 1. 模块说明
```text
├─xdctool-autoconfigure                 -- 自动配置模块                    
├─xdctool-core-all                      -- 所有工具包引入模块            
├─xdctool-crontab                       -- CRON表达式模块     
├─xdctool-dependencies                  -- 工具包版本控制模块, 类型Spring Cloud dependencies             
├─xdctool-exceptions                    -- 全局异常模块               
├─xdctool-httpclient                    -- HTTP 调用模块                   
├─xdctool-jpa                           -- ORM(JPA)模块            
├─xdctool-mail                          -- 邮件模块       
├─xdctool-mybatis                       -- ORM(Mybatis)模块                          
├─xdctool-pager                         -- 分页模块                         
├─xdctool-redis                         -- Redis模块                         
├─xdctool-swagger                       -- Swagger模块                        
└─xdctool-swagger-gateway               -- Swagger 网关模块  
└─xdctool-core                          -- 核心包                  
```

## 2. 使用说明
    详见各个模块

## 3. 注意事项
    1. 需要使用那个模块直接引入即可，
    2. "xdctool-all"引入后无须引入其他模块   
    3. "xdctool-dependencies"引入后可自定义引入指定模块，无须指定版本
    4. "xdctool-swagger-gateway" 只可网关服务使用, 如：Spring Cloud Zuul, Spring Cloud Gateway

































