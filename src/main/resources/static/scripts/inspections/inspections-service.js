'use strict';

angular.module('enterprise-quality').service('InpectionService', function($http) {
	// todo restful接口通信
    var service = {
        getList: function (param) {
            console.log(param);
            return [{
                "id": "1000",
                "model": "iVMS-0000",
                "name": "海康威视xxxxxx系统软件",
                "version": "/",
                "testType": "委托检测",
                "company": "杭州海康威视系统技术有限公司",
                "basis": "GB/T xxxx",
                "awardDate": "2011/7/15",
                "docNo": "110100000031",
                "url": "http://cert.hikvision.com.cn/xxxxxxx.jsp",
                "organization": "浙江省安全技术质量检验中心",
                "remarks": "无",
                "createDate": "2013/7/19",
                "updateDate": "2013/8/23",
                "operator": "陈晓琳"
            }, {
                "id": "1001",
                "model": "iVMS-0000",
                "name": "海康威视xxxxxx系统软件2",
                "version": "/",
                "testType": "委托检测",
                "company": "杭州海康威视系统技术有限公司",
                "basis": "GB/T xxxx",
                "awardDate": "2011/7/15",
                "docNo": "110100000032",
                "url": "http://cert.hikvision.com.cn/xxxxxxx.jsp",
                "organization": "浙江省安全技术质量检验中心",
                "remarks": "无",
                "createDate": "2013/7/19",
                "updateDate": "2013/8/23",
                "operator": "陈晓琳"
            }, {
                "id": "1003",
                "model": "iVMS-0000",
                "name": "海康威视xxxxxx系统软件3",
                "version": "/",
                "testType": "委托检测",
                "company": "杭州海康威视系统技术有限公司",
                "basis": "GB/T xxxx",
                "awardDate": "2011/7/15",
                "docNo": "110100000032",
                "url": "http://cert.hikvision.com.cn/xxxxxxx.jsp",
                "organization": "浙江省安全技术质量检验中心",
                "remarks": "无",
                "createDate": "2013/7/19",
                "updateDate": "2013/8/23",
                "operator": "陈晓琳"
            }];
        }
    };
    return service;
});
