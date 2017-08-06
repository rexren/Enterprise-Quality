# Hephaestus
Hephaestus 是一个为我们产品经理设计的小平台，也是一个基于spring-boot的开源练手项目。名字来源于希腊古神 赫菲斯托斯，希腊神话中的火神、砌石之神、雕刻艺术之神与手艺异常高超的铁匠之神，奥林匹斯十二主神之一。能建筑神殿，制作各种武器和金属用品，技艺高超，被认为工匠的始祖。在手工业中心，他被奉为锻造的庇护神。
之所以取这个名字是希望这个小平台像这位古神一样孕育出事业部那些产品，并且每个产品背后都包含着大家的工匠精神。

[API文档位置](http://api.hikvision.com.cn/dashboard/#!/project/v1ygHEESy)

## Develop guide


jvm启动后访问：[http://localhost:8080](http://localhost:8080)，自动跳转至http://localhost:8080/index.html


## 功能
软件计划包含两个主要功能模块
1.产品组件管理，产品功能注册、检索; 组件端口注册、查询等功能。
2.软件检测认证管理


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

