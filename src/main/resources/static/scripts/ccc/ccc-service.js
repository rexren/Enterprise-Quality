'use strict';

angular.module('enterprise-quality').service('CCCService', function($http) {
	// todo restful接口通信
    var service = {
        getList: function (param) {
            console.log(param);
            return [
                {
                    "id": "10000",
                    "docNo": "201401000002",
                    "model": "iVMS0000，iV0D000",
                    "productName": "离线取证终端（笔记型电脑）",
                    "awardDate": "2014/10/22",
                    "expiryDate": "2017/10/17",
                    "url": "http://cert.hikvision.com.cn/HikCert/detail?xx.jsp",
                    "organization": "中国质量认证中心",
                    "remarks": "无",
                    "createDate": "2013/7/19",
                    "updateDate": "2013/8/23",
                    "operator": "陈晓琳"
                },
                {
                    "id": "10001",
                    "docNo": "201300004200",
                    "model": "iVMS-0000",
                    "productName": "网络存储设备（服务器）",
                    "awardDate": "2015/1/6",
                    "expiryDate": "2018/8/28",
                    "url": "http://cert.hikvision.com.cn/HikCert/detail?xxx.jsp",
                    "organization": "中国质量认证中心",
                    "remarks": "无",
                    "createDate": "2013/7/19",
                    "updateDate": "2013/8/23",
                    "operator": "陈晓琳"
                },
                {
                    "id": "10002",
                    "docNo": "201300000033",
                    "model": "iVMS-0000",
                    "productName": "网络存储设备（服务器）",
                    "awardDate": "2015/1/6",
                    "expiryDate": "2018/8/28",
                    "url": "http://cert.hikvision.com.cn/HikCert/detail?xxx.jsp",
                    "organization": "中国质量认证中心",
                    "remarks": "无",
                    "createDate": "2013/7/19",
                    "updateDate": "2013/8/23",
                    "operator": "陈晓琳"
                },
                {
                    "id": "10003",
                    "docNo": "2013000042000",
                    "model": "iVMS-0000",
                    "productName": "网络存储设备（服务器）",
                    "awardDate": "2015/1/6",
                    "expiryDate": "2018/8/28",
                    "url": "http://cert.hikvision.com.cn/HikCert/detail?xxx.jsp",
                    "organization": "中国质量认证中心",
                    "remarks": "无",
                    "createDate": "2013/7/19",
                    "updateDate": "2013/8/23",
                    "operator": "陈晓琳"
                }
            ];
        }
    };
    return service;
});
