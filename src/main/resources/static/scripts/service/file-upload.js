/**
 * @ngdoc service
 * @name enterprise-FileUploadService
 * @description
 * FileUploadService
 * Service in enterprise-quality
 */

angular.module('enterprise-quality')
  .service('FileUploadService', function () {
	function UploadFile(_id, _name, _size, _file) {
        var that = this;
        var id, name, size, file, progress, status, url;

        that.getId = function() {
            return id;
        };

        that.setId = function(_id) {
            id = _id;
        };

        that.getName = function() {
            return name;
        };

        that.setName = function(_name) {
            name = _name;
        };

        that.getSize = function() {
            return size;
        };

        that.setSize = function(_size) {
            size = _size;
        };

        that.getProgress = function() {
            return progress;
        };

        that.setProgress = function(_progress) {
            progress = _progress;
        };

        that.getStatus = function() {
            return status;
        };

        that.setStatus = function(_status) {
            status = _status;
        };

        that.getUrl = function() {
            return url;
        };

        that.setUrl = function(_url) {
            url = _url;
        };

        that.getFile = function() {
            return file;
        };
        that.setFile = function(_file) {
            file = _file;
        };
        that.setId(_id);
        that.setName(_name);
        that.setSize(_size);
        that.setFile(_file);
    }
    var fileCache = {};
    var flashObjCache = {};
    var uploadBlockCache = {};
    var fid = 0;
    var uidBase = 100000000;
    function uid() {
        return 'nativeupload_' + (uidBase++);
    }
    function createInput(name,attr) {
        var input = $(document.createElement('input'));
        var inputAttr={
            'id': name,
            'type': 'file',
            multiple: false,
            accept: 'image/png,image/jpg,image/jpeg'
        };
        if(attr){
            $.extend(inputAttr,attr);
        }
        input.attr(inputAttr);
        // accept="image/png,image/jpg,image/jpeg"
        input.css('visibility', 'hidden');
        // ie不添加到body会有问题
        input.appendTo('body');
        return input;
    }
    function uploadFile(uploadBlock, file, input, uploadUrl, defer,datas) {
        var xhr = new XMLHttpRequest();
        var fileName = file.name;
        var decodeName;
        try {
            decodeName = decodeURIComponent(fileName)
        } catch (e) {
            decodeName = fileName;
        }
        fid++;
        // 验证通过之后
        var upFile = new UploadFile(fid, decodeName, file.size);
        if (xhr.upload) {
            xhr.upload.onprogress = function(_evt) {
                if (_evt.lengthComputable) {
                    var progress = parseInt(_evt.loaded / upFile.getSize() * 100, 10);
                    upFile.setProgress(progress);
                    defer.notify(uploadBlock, 'upload', upFile);
                }
            }
        }
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                xhr.onreadystatechange = null;
                var res = JSON.parse(xhr.responseText) || {
                    code: xhr.status || 'FAIL'
                };
                var code = res.code;
                if (code === 200) {
                    var url = res.data;
                    upFile.setUrl(url);
                    defer.notify(uploadBlock, 'done', upFile);
                } else {
                    defer.notify(uploadBlock, 'fail', upFile);
                }
                input.remove();
            }
        };
        // 请求初始化
        xhr.open("POST", uploadUrl);
        var data = new FormData();
        data.append("file", file);
        if(datas){
            for(var i in datas){
                data.append(i,datas[i]);
            }
        }
        defer.notify(uploadBlock, 'start', upFile);
        xhr.send(data);
    }

    function openFiles(uploadBlock, uploadUrl, defer,datas,accept) {
        var uploadAttachInputId = uid();
        var input = createInput(uploadAttachInputId,{accept:accept});
        flashObjCache[uploadAttachInputId] = input;
        input.change(function(_event) {
            // fetch FileList object
            var files = _event.target.files || _event.dataTransfer.files;
            // 验证通过之后
            uploadFile(uploadBlock, files[0], input, uploadUrl, defer,datas);
            // 重置一下input         
            try {
                _event.target.value = '';
            } catch (e) {
            }
        });
        // 出错删除
        input.trigger('click');
    }
    function open(uploadBlock,defer,accept){
        var uploadAttachInputId = uid();
        var input = createInput(uploadAttachInputId,{accept:accept});
        flashObjCache[uploadAttachInputId] = input;
        input.change(function(_event) {
            // fetch FileList object
            var files = _event.target.files || _event.dataTransfer.files;
            //console.log(files[0].name);
            defer.notify(files[0]);        
            try {
                _event.target.value = '';
            } catch (e) {
            }
        });
        // 出错删除
        input.trigger('click');
    }
      var service = {
      		openFiles : function (uploadBlock, defer) {

              var url=(defer.params&&defer.params.url);
              var datas=defer.params&&defer.params.datas;
              var accept=defer.params&&defer.params.accept;
      		  openFiles(uploadBlock, url, defer,datas,accept);
      		},
            open : function(uploadBlock,defer){//仅打开文件
                var accept=defer.params&&defer.params.accept;
                open(uploadBlock,defer,accept);
            }
      };
      return service;
  });
