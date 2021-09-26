layui.extend({
    http: '{/}../../static/js/http'
});
layui.use(['form', 'layer', 'http', 'jquery'], function () {
    const form = layui.form,
        layer = layui.layer;
    let http = layui.http;
    let $ = layui.jquery;
    form.render();
    //自定义验证规则
    form.verify({});
    //监听提交
    form.on('submit(add)', function (data) {
        let info = {
            configName: data.field.configName,
            configKey: data.field.configKey,
            configValue: data.field.configValue,
            configDesc: data.field.configDesc
        }
        layer.load();
        $.ajax({
            url: http.baseApiUrl + '/manager/config/save',
            type: "post",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify(info),
            success: function (res) {
                layer.closeAll('loading');
                if (res.rtnCode === '0') {
                    layer.alert("修改成功", {icon: 6}, function () {
                        // 获得frame索引
                        const index = parent.layer.getFrameIndex(window.name);
                        parent.location.reload();
                        //关闭当前frame
                        parent.layer.close(index);
                    });
                } else {
                    layer.msg(res.rtnMsg);
                }
            }
        })
        return false;
    });
});
