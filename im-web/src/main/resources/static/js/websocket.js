var ws = {//登入函数 将用户信息注入Constant.onlineUserMap对象
    login: function () {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                token :token,
                type: "login",
                ip: ip
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },

    singleSend: function (fromUserId, toUserId, content) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId": fromUserId,
                "toUserId": toUserId,
                "content": content,
                "type": "SINGLE_SENDING"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },

    groupSend: function (fromUserId, toGroupId, content) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId": fromUserId,
                "toGroupId": toGroupId,
                "content": content,
                "type": "GROUP_SENDING"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },

    fileMsgSingleSend: function (fromUserId, toUserId, originalFilename, fileUrl, fileSize) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId": fromUserId,
                "toUserId": toUserId,
                "originalFilename": originalFilename,
                "fileUrl": fileUrl,
                "type": "FILE_MSG_SINGLE_SENDING"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },

    fileMsgGroupSend: function (fromUserId, toGroupId, originalFilename, fileUrl, fileSize) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            var data = {
                "fromUserId": fromUserId,
                "toGroupId": toGroupId,
                "originalFilename": originalFilename,
                "fileUrl": fileUrl,
                "type": "FILE_MSG_GROUP_SENDING"
            };
            socket.send(JSON.stringify(data));
        } else {
            alert("Websocket连接没有开启！");
        }
    },
    loginReceive: function () {
        console.log("token为 " + token + " 的用户登记到在线用户表成功！");
    },

    singleReceive: function (data) {
        // 获取、构造参数
        console.log(data);
        var fromUserId = data.fromUserId;
        var content = data.content;
        var fromAvatarUrl;
        var selectUserId = $('#toUserId').val();
        var flag = false;
        if(selectUserId == fromUserId){
            flag = true;
        }
        var $receiveLi;
        $('#conLeft').find('span.hidden-userId').each(function () {
            console.log(this.innerHTML);
            if (this.innerHTML == fromUserId) {
                $receiveLi = $(this).parent(".liLeft").parent("li");
            }
        })
        fromAvatarUrl = friendMap.get(fromUserId).avatarUrl;
        var answer = '';
        answer += '<li>' +
            '<div class="answers">' + content + '</div>' +
            '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
            '</li>';

        // 消息框处理
        processMsgBox.receiveSingleMsg(answer, fromUserId);
        // 好友列表处理
        processFriendList.receiving(content, $receiveLi, flag);
    },

    groupReceive: function (data) {
        // 获取、构造参数
        console.log(data);
        var fromUserId = data.fromUserId;
        var content = data.content;
        var toGroupId = data.toGroupId;
        var fromAvatarUrl;
        fromAvatarUrl = friendMap.get(fromUserId).avatarUrl;
        var selectGroupId = $('#toGroupId').val();
        var flag = false;
        if(selectGroupId == toGroupId && selectGroupId!=""){
            flag = true;
        }
        console.log(selectGroupId);
        console.log(toGroupId);
        console.log(flag);
        var $receiveLi;
        $('#conLeft').find('span.hidden-groupId').each(function () {
            if (this.innerHTML == toGroupId) {
                $receiveLi = $(this).parent(".liLeft").parent("li");
            }
        })
        var answer = '';
        answer += '<li>' +
            '<div class="answers">' + content + '</div>' +
            '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
            '</li>';
        // 消息框处理
        processMsgBox.receiveGroupMsg(answer, toGroupId);
        // 好友列表处理
        processFriendList.receiving(content, $receiveLi, flag);
    },

    fileMsgSingleRecieve: function (data) {
        // 获取、构造参数
        console.log(data);
        var fromUserId = data.fromUserId;
        var originalFilename = data.originalFilename;
        var fileUrl = data.fileUrl;
        var content = "[文件]";
        var fromAvatarUrl;
        fromAvatarUrl = friendMap.get(fromUserId).avatarUrl;
        var selectUserId = $('#toUserId').val();
        var flag = false;
        if(selectUserId == fromUserId){
            flag = true;
        }
        var $receiveLi;
        $('#conLeft').find('span.hidden-userId').each(function () {
            if (this.innerHTML == fromUserId) {
                $receiveLi = $(this).parent(".liLeft").parent("li");
            }
        })
        var fileHtml =
            '<li>' +
            '<div class="receive-file-shown">' +
            '<div class="media">' +
            '<div class="media-body"> ' +
            '<h5 class="media-heading">' + originalFilename + '</h5>' +
            '<span>' + fileSize + '</span>' +
            '</div>' +
            '<a href="' + fileUrl + '" class="media-right" target="_blank">' +
            '<i class="glyphicon glyphicon-file" style="font-size:28pt;"></i>' +
            '</a>' +
            '</div>' +
            '</div>' +
            '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
            '</li>';

        // 消息框处理
        processMsgBox.receiveSingleMsg(fileHtml, fromUserId);
        // 好友列表处理
        processFriendList.receiving(content, $receiveLi,flag);
    },

    fileMsgGroupRecieve: function (data) {
        // 1. 获取、构造参数
        console.log(data);
        var fromUserId = data.fromUserId;
        var toGroupId = data.toGroupId;
        var originalFilename = data.originalFilename;
        var fileUrl = data.fileUrl;
        var content = "[文件]";
        var fromAvatarUrl;
        fromAvatarUrl = friendMap.get(fromUserId).avatarUrl;
        var selectGroupId = $('#toGroupId').val();
        var flag = false;
        if(selectGroupId == toGroupId){
            flag = true;
        }
        var $receiveLi;
        $('.conLeft').find('span.hidden-groupId').each(function () {
            if (this.innerHTML == toGroupId) {
                $receiveLi = $(this).parent(".liLeft").parent("li");
            }
        })
        var fileHtml =
            '<li>' +
            '<div class="receive-file-shown">' +
            '<div class="media">' +
            '<div class="media-body"> ' +
            '<h5 class="media-heading">' + originalFilename + '</h5>' +
            '</div>' +
            '<a href="' + fileUrl + '" class="media-right" target="_blank" >' +
            '<i class="glyphicon glyphicon-file" style="font-size:28pt;"></i>' +
            '</a>' +
            '</div>' +
            '</div>' +
            '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
            '</li>';

        // 2. 消息框处理
        processMsgBox.receiveGroupMsg(fileHtml, toGroupId);
        // 3. 好友列表处理
        processFriendList.receiving(content, $receiveLi, flag);
    },

    remove: function () {
        socket.close();
    }
};

function logout() {
    // 1. 关闭websocket连接
    ws.remove();

    // 2. 注销登录状态
    $.ajax({
        type: 'POST',
        url: 'logout',
        dataType: 'json',
        async: true,
        success: function (data) {
            if (data.status == 200) {
                // 3. 注销成功，进行页面跳转
                console.log("注销成功！");
                window.location.href = "login";
            } else {
                alert(data.msg);
            }
        }
    });
}