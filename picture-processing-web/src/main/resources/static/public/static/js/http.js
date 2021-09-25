/*
 * @Author: https://github.com/WangEn
 * @Author: https://gitee.com/lovetime/
 * @Date:   2020-07-29
 * @lastModify 2020-07-30 16:45:40
 * +----------------------------------------------------------------------
 * | Weadmin [ 后台管理模板 ]
 * | 基于Layui http://www.layui.com/
 * +----------------------------------------------------------------------
 */
layui.define(['jquery', 'layer'], function (exports) {
    let $ = layui.jquery,
        layer = layui.layer;

    let baseApiUrl = 'http://localhost:8081/service';

    let request = {
        baseApiUrl: baseApiUrl,
        login: function (obj) {
            layer.load();
            $.ajax({
                url: baseApiUrl + '/manager/login',
                type: 'POST',
                data: {
                    username: obj.username,
                    password: obj.password
                },
                success: function (res) {
                    layer.closeAll('loading');
                    if (res.rtnCode === '0') {
                        localStorage.setItem('userinfo', JSON.stringify(res.result))
                        layer.msg('登录成功，即将跳转~');
                        location.href = './index.html'
                    } else {
                        layer.msg(res.rtnMsg);
                    }
                }
            })
        },
        logout: function () {
            layer.load();
            $.ajax({
                url: baseApiUrl + '/manager/logout',
                type: 'POST',
                success: function (res) {
                    layer.closeAll('loading');
                    if (res.rtnCode === '0') {
                        localStorage.removeItem('userinfo')
                        layer.msg('登出成功，即将跳转~', function () {
                            top.location.href = './login.html';
                        });
                    } else {
                        layer.msg(res.rtnMsg);
                    }
                }
            })
        },
        getMenu: function (userinfo) {
            let that = this;
            if (!$('#side-nav').length) {
                return;
            }
            let menu;
            if (userinfo.admin) {
                menu = 'json/adminmenu.json';
            } else {
                menu = 'json/menu.json';
            }
            $.getJSON(menu, function (res) {
                layer.load()
                if (res.status === 0) {
                    var menuList = res.data;
                    var items = [];
                    var _ul = $('<ul></ul>').addClass('nav').attr('id', 'nav');
                    $.each(menuList, function (index, val) {
                        var icon = val.icon ? '<i class="layui-icon layui-icon-' + val.icon + '"></i>' : '<i class="layui-icon layui-icon-app"></i>';
                        var item = '<li id="menu' + val.id + '"><a _href="' + val.url + '">' + icon + '<cite>' + val.name +
                            '</cite><i class="iconfont nav_right">&#xe697;</i></a></li>';
                        items.push(item);
                        if (val.children && val.children.length) {
                            //有二级子菜单
                            setTimeout(function () {
                                that.getSubMenu(val.id, val.children);
                            }, 500);
                        }
                    });
                    let side_nav = $('#side-nav');
                    side_nav.empty();
                    _ul.append(items.join(''));
                    side_nav.append(_ul);
                } else {
                    layer.msg(res.msg, function () {
                    });
                }
                layer.closeAll('loading');
            })
        },
        /**
         * @param {Object} id 上级子菜单id,拼接menu获取元素
         * @param {Object} subList 子菜单列表
         */
        getSubMenu: function (id, subList) {
            var that = this;
            var subItems = [];
            var subUl = $('<ul></ul>').addClass('sub-menu');
            $.each(subList, function (idx, sub) {
                var subItem = '<li id="menu' + sub.id + '"><a _href="' + sub.url + '"><i class="iconfont">&#xe6a7;</i><cite>' +
                    sub.name + '</cite></a></li>';
                subItems.push(subItem);
                if (sub.children && sub.children.length) {
                    setTimeout(function () {
                        that.getSubMenu(sub.id, sub.children);
                    }, 1500);
                }
            });
            subUl.append(subItems.join(''));
            $('#menu' + id).append(subUl);
        }
    };
    exports('http', request);

});
