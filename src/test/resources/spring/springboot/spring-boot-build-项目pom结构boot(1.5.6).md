# spring-boot-build-项目pom结构boot(1.5.6) 学习笔记
# spring-boot-build(org.springframework.boot)
- spring-boot-build(pom)
      - spring-boot-dependencies(pom)
          - spring-boot-parent(pom)
              - spring-boot-tools(pom)
                  - spring-boot-autoconfigure-processor(jar)
                  - spring-boot-loader(jar)
                      - build a single jar file
              - spring-boot(jar)
                  - SpringApplication
              - spring-boot-samples
              - spring-boot-test(jar)
              - spring-boot-autoconfigure(jar)
                  - @EnableAutoConfiguration
              - spring-boot-starters(jar)
                  - a set of convenient dependency
                  - spring-boot-starter
                  - spring-boot-starter-data-jpa
                  - spring-boot-starter-tomcat
              - spring-boot-actuator(jar)
              - spring-boot-docs(jar)
              - spring-boot-cli(jar)
                  - Spring command line application
## spring-boot-samples
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git