# spike-system
upload on 2019-07-22

## 目前完成内容

1. 前端页面
2. Eureka服务注册中心
3. 商品创建查询服务
4. 帐号认证中心

## 待完成内容

1. 客户端服务
2. 前后端对接
3. 下单服务
4. 支付服务
6. 数据库同步服务

## 秒杀流程设计

### 注册/登录流程图

![register&login](http://qiniuyun.gtouyang.com/%E6%B3%A8%E5%86%8C&%E7%99%BB%E5%BD%95%E6%B5%81%E7%A8%8B%E5%9B%BEv1.png)

注册完之后会自动发起登录

### 下单流程图

![order](http://qiniuyun.gtouyang.com/%E4%B8%8B%E5%8D%95%E6%B5%81%E7%A8%8B%E5%9B%BEv1.png)

1. 在Kafka中放入下单消息（user，productId， amount）
2. `order-service`接收到消息，开始处理订单
   1. 检查本地缓存中该商品是否已售罄，若售罄直接返回库存不足到Kafka中
   2. 检查Redis缓存中的库存，并尝试减库存，减库存失败则返回库存不足到Kafka中，并在本地缓存中设置该商品已售罄，5分钟内不再接受该商品下单
   3. 减DBLE中的库存，若失败则则返回库存不足到Kafka中，并在本地缓存中设置该商品已售罄，5分钟内不再接受该商品下单
   4. 生成订单放入Redis中，在Kafka中放入下单成功的消息
3. 客户端轮询Kafka和Redis检查有没有自己的订单，或者自己的订单是否失败
4. `sync-service`接收到订单已完成的消息，同步到DBLE中

### 支付流程图

![pay](http://qiniuyun.gtouyang.com/%E6%94%AF%E4%BB%98%E6%B5%81%E7%A8%8B%E5%9B%BEv1.png)

1. 根据用户名获取钱包列表
2. 用户选择钱包，并输入支付密码
3. 客户端将支付消息放入Kafka中
4. `pay-service`接收到消息开始处理
   1. 从数据库中获取该钱包信息
   2. 比对余额和支付密码，余额不足或支付密码错误则返回支付失败到Kafka中
   3. 减余额，然后设置订单状态为已完成，放入Kafka中
5. `sync-service`接收到订单已完成的消息，同步到DBLE中
