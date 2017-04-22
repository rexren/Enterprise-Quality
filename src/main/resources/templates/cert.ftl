<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>公安事业部 | 证书检索平台</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"/>
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <!-- Font Awesome -->
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css"/>
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css"/>
    <!-- Theme style -->
    <link rel="stylesheet" href="dist/css/AdminLTE.min.css"/>
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
          page. However, you can choose any other skin. Make sure you
          apply the skin class to the body tag so the changes take effect.
    -->
    <link rel="stylesheet" href="dist/css/skins/skin-blue.min.css"/>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries-->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]-->
    <script src="/plugins/html5shiv/html5shiv.min.js"></script>
    <script src="/plugins/respond/respond.min.js"></script>
    <!--[endif]-->
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<#include "./common/header.ftl">
<#include "./common/sidebar.ftl">
<#include "./common/new-cert.ftl">

    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>公检国标
                <small>针对GB/28181的检测</small>
            </h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">

                        <div class="box-header">
                            <h3 class="box-title">公检国标</h3>


                            <div class="box-tools">
                                <div class="input-group input-group-sm" style="width: 150px;">
                                    <input type="text" name="table_search" class="form-control pull-right"
                                           placeholder="Search">

                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                                        </button>
                                    </div>

                                    <div class="input-group-btn">
                                        <button id="add-new-cert" type="add" class="btn btn-default"><i
                                                class="fa fa-plus-square"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="example2" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>型号</th>
                                    <th>名称</th>
                                    <th>颁发日期</th>
                                    <th>文件编号</th>
                                    <th>链接地址</th>
                                    <th>详情</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list certs as cert>
                                <tr>
                                    <td>${cert_index + 1}</td>
                                    <td>${cert.productType}</td>
                                    <td>${cert.name}</td>
                                    <td>${cert.awardDate?string("yyyy-MM-dd")}</td>
                                    <td>${cert.docSerial}</td>
                                    <td><a href="${cert.certUrl}" target="_blank">${cert.certUrl}</a></td>
                                    <td><a>点击查看详情</a></td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>
            </div>
        </section>


    </div>
</div>
<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->

<!-- jQuery 2.2.3 -->
<script src="plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>

<script src="pages/cert.js"></script>

</body>
</html>
