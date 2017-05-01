<div class="modal" id="new-inspection">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title">新建</h4>
            </div>
            <form role="form" action="inspection/create" method="post">
                <div class="modal-body">

                    <div id="new-cert-form" class="box-body">
                        <div class="form-group">
                            <label for="pro-type">产品型号</label>
                            <input type="text" , class="form-control" id="pro-type" name="productType"
                                   placeholder="请输入产品型号">
                        </div>

                        <div class="form-group">
                            <label for="pro-name">软件名称</label>
                            <input type="text" , class="form-control" id="pro-name" name="name" placeholder="请输入产品名称">
                        </div>

                        <div class="form-group">
                            <label for="pro-version">软件版本</label>
                            <input type="text" , class="form-control" id="pro-version" name="version"
                                   placeholder="请输入版本">
                        </div>

                        <div class="form-group">
                            <label for="test-type">测试类别</label>
                            <input type="text" , class="form-control" id="test-type" name="testType"
                                   placeholder="请输入测试类别">
                        </div>

                        <div class="form-group">
                            <label for="company">受检单位</label>
                            <input type="text" , class="form-control" id="company" name="company" placeholder="请输入受检单位">
                        </div>

                        <div class="form-group">
                            <label for="basis">测试依据</label>
                            <input type="text" , class="form-control" id="basis" name="basis" placeholder="请输入测试依据">
                        </div>

                        <div class="form-group">
                            <label for="basis">颁发日期</label>

                            <div class="input-group date form_date" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                                <input class="form-control" type="text" name="awardDate" value="" readonly>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>

                        </div>

                        <div class="form-group">
                            <label for="doc-serial">文件编号</label>
                            <input type="text" , class="form-control" id="doc-serial" name="docSerial"
                                   placeholder="请输入文件编号">
                        </div>

                        <div class="form-group">
                            <label for="cert-url">证书系统链接</label>
                            <input type="text" , class="form-control" id="cert-url" name="certUrl"
                                   placeholder="请输入证书系统链接">
                        </div>

                        <div class="form-group">
                            <label for="test-orgnization">认证机构</label>
                            <input type="text" , class="form-control" id="test-orgnization" name="testOrgnization"
                                   placeholder="请输入认证机构">
                        </div>

                        <div class="form-group">
                            <label for="remark">备注</label>
                            <input type="text" , class="form-control" id="remark" name="remark" placeholder="请输入备注信息">
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="submit" id="submit-new-cert" class="btn btn-primary">保存</button>
                </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->