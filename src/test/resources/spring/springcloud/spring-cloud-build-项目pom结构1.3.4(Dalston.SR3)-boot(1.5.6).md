# spring-cloud-build-项目pom结构1.3.4(Dalston.SR3)-boot(1.5.6) 学习笔记
# spring-cloud-module分析(org.springframework.cloud)
- spring-cloud-build(pom)
      - spring-cloud-starter-build(pom)
          - 用于映射Dalston.SR3与spring-cloud-build之间关系
      - spring-cloud-dependencies-parent(pom)
      - spring-cloud-build-dependencies(依赖springboot版本)
          - spring-boot-dependencies
      - spring-cloud-build-tools
      - spring-cloud-commons-parent(pom)
          - spring-cloud-commons-dependencies(pom)
          - spring-cloud-context
          - spring-cloud-commons
          - spring-cloud-starter
              - spring-boot-starter
      - spring-cloud-netflix(pom)----1.3.3.RELEASE
          - spring-cloud-starter-netflix(pom)
              - spring-cloud-starter-netflix-zuul
              - spring-cloud-starter-netflix-eureka-server
                  - spring-cloud-netflix-eureka-server
                      - eureka-core
                      - eureka-client
      - spring-cloud-zookeeper(pom)
          - spring-cloud-starter-zookeeper
              - spring-cloud-zookeeper-core
      - spring-cloud-config(pom)
          - spring-cloud-starter-config
              - spring-cloud-config-client
## spring-cloud-netflix(pom)----1.3.3.RELEASE
### Service Discovery (Eureka)
- Eureka instances can be registered and clients can discover the instances using Spring-managed beans
### Circuit Breaker (Hystrix)
- Hystrix clients can be built with a simple annotation-driven method decorator
### Intelligent Routing (Zuul)
-  automatic registration of Zuul filters, and a simple convention over configuration approach to reverse proxy creation
### Client Side Load Balancing (Ribbon)
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git