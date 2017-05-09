$(function () {
    "use strict";

    // 测试前后端通信
    $.get('/inspections/list.action', {num:1}, function (data, status) {
        console.log('数据：'+data + '\n状态：' + status);
    });

    var ajaxResult = {
        "code": 200,
		"data": {
            "pagination": {
                "page": 0,
                "size": 10,
                "totalPage": 3,
                "total": 27
            },
            'result': [{
                maintenaner: 'CXL',
                productType: 'iVMS-8200-FAS',
                name: '公安视频图像信息综合应用平台',
                version: 'V1.5',
                testType: '委托检测',
                company: '杭州海康威视系统技术有限公司',
                basis: 'GA/T 000-000',
                awardDate: '2017-01-01',
                docSerial: 'XXX0000号',
                certUrl: 'http://cert.hikvision.com.cn/xxxxxx.jsp',
                testOrgnization: 'x所',
                remark: '人脸',
                createDate: '2017-01-01',
                updateDate: '2017-01-01'
            }, {
            	  maintenaner: 'CXL',
                  productType: 'iVMS-8200-FAS',
                  name: '公安视频图像信息综合应用平台',
                  version: 'V1.9',
                  testType: '委托检测',
                  company: '杭州海康威视系统技术有限公司',
                  basis: 'GA/T 000-000',
                  awardDate: '2017-02-01',
                  docSerial: 'XXX0001号',
                  certUrl: 'http://cert.hikvision.com.cn/xxxxxx2.jsp',
                  testOrgnization: 'x所',
                  remark: '人脸',
                  createDate: '2017-02-01',
                  updateDate: '2017-02-01'
            }]
        }
    };

});
