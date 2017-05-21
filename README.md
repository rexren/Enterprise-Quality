# Enterprise-Quality
资质检索

为了我们可爱的产品经理们做的

[API文档位置](http://api.hikvision.com.cn/dashboard/#!/project/v1ygHEESy)

## 调试准备
### 运行搜索引擎

调试需要先启动elasticsearch 2.4.5, 建议使用docker方式启动

下载
``` bash
docker pull hub.c.163.com/library/elasticsearch:2.4.5
```

这个版本没有安装head插件，所以先安装
```Dockerfile
FROM hub.c.163.com/library/elasticsearch:2.4.5

WORKDIR /usr/share/elasticsearch

RUN ./bin/plugin install mobz/elasticsearch-head

EXPOSE 9200 9300
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["elasticsearch"]
```
运行docker
``` bash
docker run -d --name elas -p 9300:9300 -p 9200:9200  es
```

访问以下链接查看是否启动成功
http://127.0.0.1:9200/_plugin/head/

### 启动web服务

jvm启动后访问：[http://localhost:8080](http://localhost:8080)，自动跳转至http://localhost:8080/index.html


## 功能
### 页面分类
1. 登录页
1. 更新说明 - 摘要列表页
1. 更新说明 - 新建/编辑/查看详情页
1. 公检国标 - 摘要列表页
1. 公检国标 - 新建/编辑/查看详情页
1. 公检国标 - 检索结果页
1. 双证 - 摘要列表页
1. 双证 - 新建/编辑/查看详情页
1. 双证 - 检索结果页
1. 3C认证 - 摘要列表页
1. 3C认证 - 新建/编辑/查看详情页
1. 3C认证 - 检索结果页
1. 用户管理页（增&删功能）（管理员独有）

### 需求
1. 数据操作和权限管理
2. 现有数据清洗和导入
3. 所有分类下的深度检索
4. 控制权限

## 阶段性任务划分
1. 获取三大列表（inspections/copyright/ccc）数据及其详细内容
2. 新增列表条目和修改列表条目
3. 导入现在已有的excel文档到数据库中
4. 能够简单检索
5. 能够显示更新信息
6. 能够导入和解析检测项索引表的excel文档到数据库中
7. 能够深度检索检测项内容

