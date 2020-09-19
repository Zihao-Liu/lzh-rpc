<p align="center">
    <h3 align="center">LZH-RPC</h3>
    <p align="center">
        用来学习RPC实现原理的项目。在实现RPC功能基础上，提供注册中心、权限控制、负载均衡、链路追踪和限流熔断等服务治理功能。
        <br>
        <a href="https://zihao-liu.github.io/lzh-rpc/"><strong>-- 中文文档 --</strong></a>
    </p> 
    <img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/Zihao-Liu/lzh-rpc">
    <img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/Zihao-Liu/lzh-rpc/Java%20CI%20with%20Maven">
</p>


## Introduction

LZH-RPC 是一个用来学习RPC实现原理的项目。在实现RPC功能基础上，提供注册中心、权限控制、负载均衡、链路追踪和限流熔断等服务治理功能。

项目中实现功能的方式多数来自于借鉴业界已有的成熟方案，也有一些是自己的想法，并不能保证合理性与正确性。

## Dream Features

- 1、基础配置：支持Spring项目通过注解方式进行快速接入；
- 2、服务透明：封装了远程调用的细节，调用方调用时只需要引入Rpc和服务方依赖，即可像调用本地方法一样调用；
- 3、序列化：提供多种序列化方案，支持 HESSIAN、KYRO、PROTOSTUFF 等方案；
- 4、注册中心：支持使用自研的注册中心进行服务发现；也可以使用IP直连的方式进行调用，适合测试联调使用；
- 5、负载均衡：提供多种负载均衡方法，支持 随机、轮询、IP哈希、最小连接数和最短响应时间等；
- 6、容错策略：服务下线实时广播；提供不同的容错策略，节点不可用时进行摘除；
- 7、权限控制：服务调用增加权限控制，提供服务级别、接口级别和方法级别的控制；
- 8、链路追踪：增加链路信息记录，包括响应时间和返回码的打点上报等；
- 9、限流熔断：主调方支持配置限流策略；被调方支持熔断策略；
- 10、服务监控：基于链路追踪实现服务监控，包括调用统计，响应时间和异常情况等；
- 11、可视化服务：提供一些配置操作的可视化界面；

详细规划可以查看[Projects看板](https://github.com/Zihao-Liu/lzh-rpc/projects)

## More