$(
    function () {
        let wxAuthorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        var openId;
        //获取code参数值.进行用户授权
        let code = getQueryVariable("code");
        if (code == null || code === "") {
            $.get("/service/config/getAppId", function (data) {
                let appId = data.result;
                let url = window.location.href;
                window.location.href = wxAuthorizeUrl + "appid=" + appId + "&redirect_uri=" + encodeURIComponent(url) + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
            });
        } else {
            $.get("/service/config/userAuthorize", {"code": code}, function (data) {
                    openId = data.result;
                    alert(openId);
                }
            );

        }
        $.get("/service/config/getWXConfig", {"url": window.location.href}, function (data) {
            if (data === null) {
                return;
            }
            wx.config({
                debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: data.result.appId, // 必填，公众号的唯一标识
                timestamp: data.result.timestamp, // 必填，生成签名的时间戳
                nonceStr: data.result.nonceStr, // 必填，生成签名的随机串
                signature: data.result.signature,// 必填，签名
                jsApiList: [] // 必填，需要使用的JS接口列表
            });
        });

        /**
         * 从当前url中获取参数值
         * @param variable 参数key
         * @returns {string}
         */
        function getQueryVariable(variable) {
            let query = window.location.search.substring(1);
            let vars = query.split("&");
            for (let i = 0; i < vars.length; i++) {
                let pair = vars[i].split("=");
                if (pair[0] === variable) {
                    return pair[1];
                }
            }
            return "";
        }
    }
)
;
