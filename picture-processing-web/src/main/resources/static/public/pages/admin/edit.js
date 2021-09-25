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
    form.verify({
        pass: [/(.+){8,12}$/, '密码必须8到12位'],
        repass: function () {
            if ($('#L_pass').val() !== $('#L_repass').val()) {
                return '两次密码不一致';
            }
        }
    });
    let logUserInfo = localStorage.getItem('userinfo');
    let logUser = JSON.parse(logUserInfo);
    //监听提交
    form.on('submit(add)', function (data) {
        let info = {
            userName: data.field.username,
            passWord: data.field.pass,
            authority: data.field.authority
        }
        layer.load();
        $.ajax({
            url: http.baseApiUrl + '/manager/user/save',
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
                        if (logUser.userName === data.field.username) {
                            http.logout();
                        }
                    });
                } else {
                    layer.msg(res.rtnMsg);
                }
            }
        })
        return false;
    });
});
