//设置个人信息
function setUserInfo() {
    var param={"token": token};
    //console.log(JSON.stringify(param));
    $.ajax({
        type: "POST",
        url: "chatroom/getUserInfo",
        data: JSON.stringify(param),
        contentType:"application/json;charsetset=UTF-8", //必要 不加报 415,传参数据类型
        dataType: "json",//返回数据类型
        async: false,
        success: function (data) {
            if(data.success == true){
                var userInfo = data.result;
                //console.log(userInfo);
                userId = userInfo.id;
                //填充头像和名称
                $("#userName").html(userInfo.userName);
                $("#avatarUrl").attr("src", userInfo.avatarUrl);
            }else{
                alert("用户信息获取失败,返回登陆页");
                window.location.href = "index";
            }
        }
    });
}

//设置好友列表
function setFriendList() {
    $.ajax({
        type: "GET",
        url: "chatroom/getFriendList",
        data: "userId="+userId,
        dataType: "json",//返回数据类型
        success: function (data) {
            if(data.success == true){
                var friendList = data.result;
                //console.log(friendList);
                sentMessageMap = new SentMessageMap();
                //填充消息映射对象
                sentMessageMap.put(0, new Array());

                var ListHTML = "";
                ListHTML +=
                    '<li>' +
                    '<div class="liLeft"><img src="../static/img/avatar/Group01.jpg">'+
                    '<span class="hidden-groupId">' + "0" + '</span>' +
                    '<span class="name">' + "官方群组" + '</span>' +
                    '<span class="infor"></span>' +
                    '</div>' +
                    '</li>';
                //初始化userId对 userInfo映射
                friendMap = new Map();
                for(var i = 0; i < friendList.length; i++){
                    sentMessageMap.put(friendList[i].id, new Array());
                    friendMap.set(friendList[i].id,friendList[i]);
                    ListHTML +=
                        '<li>' +
                        '<div class="liLeft"><img src="' + friendList[i].avatarUrl + '">' +
                        '<span class="hidden-userId">' + friendList[i].id + '</span>' +
                        '<span class="name">' + friendList[i].userName + '</span>' +
                        '<span class="infor"></span>' +
                        '</div>' +
                        '</li>';
                }
                //设置历史消息
                setHistoryMessage();
                //填充页面左侧用户栏
                $('#conLeft ul').empty();
                $('#conLeft ul').append(ListHTML);
                // 绑定好友框点击事件
                $('#conLeft ul li').on('click', friendLiClickEvent);
            }else{
                alert("用户信息获取失败,返回登陆页");
                window.location.href = "index";
            }
        }
    });
}


function switchMessage(message) {

    var fromUserId = message.fromUserId;
    var toUserId = message.toUserId;
    var toGroupId = message.toGroupId;
    var originalFilename = message.originalFilename;
    var fileUrl = message.fileUrl;
    var fromAvatarUrl = message.fromAvatarUrl;
    var $receiveLi;
    var avatarUrl =  $('#avatarUrl').attr("src");
    switch(message.type) {
        case "SINGLE_SENDING":
            if(message.fromUserId==userId){//说明是该用户发送的消息
                var news=message.content;
                var msg = '';
                msg += '<li>' +
                    '<div class="news">' + news + '</div>' +
                    '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
                    '</li>';
                // 消息框处理：
                processMsgBox.sendMsg(msg, toUserId, toGroupId);
            }else{
                var content=message.content;
                var answer = '';
                answer += '<li>' +
                    '<div class="answers">' + content + '</div>' +
                    '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
                    '</li>';
                // 消息框处理
                processMsgBox.receiveSingleMsg(answer, fromUserId);
            }
            break;
        case "GROUP_SENDING":
            if(message.fromUserId==userId){//说明是该用户发送的消息
                var news=message.content;
                var msg = '';
                msg += '<li>' +
                    '<div class="news">' + news + '</div>' +
                    '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
                    '</li>';
                // 消息框处理：
                processMsgBox.sendMsg(msg, toUserId, toGroupId);
            }else{
                var content=message.content;
                var answer = '';
                answer += '<li>' +
                    '<div class="answers">' + content + '</div>' +
                    '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
                    '</li>';
                // 消息框处理
                processMsgBox.receiveGroupMsg(answer, toGroupId);
            }
            break;
        case "FILE_MSG_SINGLE_SENDING":
            if(message.fromUserId==userId){//说明是该用户发送的消息
                var fileHtml =
                    '<li>' +
                    '<div class="send-file-shown">' +
                    '<div class="media">' +
                    '<a href="' + fileUrl + '" class="media-left" target="_blank">' +
                    '<i class="glyphicon glyphicon-file" style="font-size:28pt;"></i>' +
                    '</a>' +
                    '<div class="media-body"> ' +
                    '<h5 class="media-heading">' + originalFilename + '</h5>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
                    '</li>';
                //文件 消息框处理：
                processMsgBox.sendFileMsg(fileHtml, toUserId, toGroupId);
            }else{
                var fileHtml =
                    '<li>' +
                    '<div class="receive-file-shown">' +
                    '<div class="media">' +
                    '<div class="media-body"> ' +
                    '<h5 class="media-heading" >' + originalFilename + '</h5>' +
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
            }
            break;
        case "FILE_MSG_GROUP_SENDING":
            if(message.fromUserId==userId){//说明是该用户发送的消息
                var fileHtml =
                    '<li>' +
                    '<div class="send-file-shown">' +
                    '<div class="media">' +
                    '<a href="' + fileUrl + '" class="media-left" target="_blank">' +
                    '<i class="glyphicon glyphicon-file" style="font-size:28pt;"></i>' +
                    '</a>' +
                    '<div class="media-body"> ' +
                    '<h5 class="media-heading">' + originalFilename + '</h5>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
                    '</li>';
                //文件 消息框处理：
                processMsgBox.sendFileMsg(fileHtml, toUserId, toGroupId);
            }else{
                var fileHtml =
                    '<li>' +
                    '<div class="receive-file-shown">' +
                    '<div class="media">' +
                    '<div class="media-body"> ' +
                    '<h5 class="media-heading">' + originalFilename + '</h5>' +
                    '</div>' +
                    '<a href="' + fileUrl + '" class="media-right" target="_blank">' +
                    '<i class="glyphicon glyphicon-file" style="font-size:28pt;"></i>' +
                    '</a>' +
                    '</div>' +
                    '</div>' +
                    '<div class="answerHead"><img src="' + fromAvatarUrl + '"/></div>' +
                    '</li>';
                // 消息框处理
                processMsgBox.receiveGroupMsg(fileHtml, toGroupId);
            }
            break;
    }

}
function setHistoryMessage() {

    $.ajax({
        type: "GET",
        url: "chatroom/getMessageList",
        data: "userId="+userId,
        dataType: "json",//返回数据类型
        success: function (data) {
            if(data.success == true){
                var MessageList = data.result;
                for(var i=0;i<MessageList.length;i++){
                    //console.log(MessageList[i]);
                    switchMessage(MessageList[i]);
                }
                $('.newsList li').empty();//清空暂存
            }else{
                alert("用户信息获取失败,返回登陆页");
                window.location.href = "index";
            }
        }
    });
}

$(".myfile").fileinput({
    uploadUrl: "imwechat/upload",
    uploadAsync: true, //默认异步上传
    showUpload: true, //是否显示上传按钮,跟随文本框的那个
    showRemove: false, //显示移除按钮,跟随文本框的那个
    showCaption: false,//是否显示标题,就是那个文本框
    showPreview: true, //是否显示预览,不写默认为true
    dropZoneTitle: "请通过拖拽图片文件放到这里",
    dropZoneEnabled: false,//是否显示拖拽区域，默认不写为true，但是会占用很大区域
    maxFileSize: 30720,//单位为kb，如果为0表示不限制文件大小
    maxFileCount: 1, //表示允许同时上传的最大文件个数
    enctype: 'multipart/form-data',
    validateInitialCount: true,
    previewFileIcon: "<i class='glyphicon glyphicon-file'></i>",
    msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
    language: 'zh'
})
//异步上传返回结果处理
$('.myfile').on('fileerror', function (event, data, msg) {
    console.log("fileerror");
    console.log(data);
});
//异步上传返回结果处理
$(".myfile").on("fileuploaded", function (event, data, previewId, index) {

    // 1. 上传成功1.5秒后自动关闭上传模态框
    //console.log("fileuploaded");
    setTimeout(function () {
        $('#upload-cancel').trigger('click');
        $('.fileinput-remove').trigger('click');
    }, 1500);

    // 2. 获取、设置参数
    /*console.log(data);
    console.log(data.response);
    console.log(data.response.result);*/
    var returnData = data.response.result;
    var originalFilename = returnData.originalFilename;
    var fileUrl = returnData.fileUrl;
    var content = "[文件]";
    var fromUserId = userId;
    var avatarUrl = $('#avatarUrl').attr("src");
    var $sendLi = $('#conLeft').find('li.bg');
    var toUserId = $('#toUserId').val();
    var toGroupId = $('#toGroupId').val();
    var fileHtml =
        '<li>' +
        '<div class="send-file-shown">' +
        '<div class="media">' +
        '<a href="' + fileUrl + '" class="media-left" target="_blank">' +
        '<i class="glyphicon glyphicon-file" style="font-size:28pt;"></i>' +
        '</a>' +
        '<div class="media-body"> ' +
        '<h5 class="media-heading">' + originalFilename + '</h5>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
        '</li>';

    // 3. 发送信息到服务器
    if (toUserId.length != 0) {
        ws.fileMsgSingleSend(fromUserId, toUserId, originalFilename, fileUrl, 0);
    } else {
        ws.fileMsgGroupSend(fromUserId, toGroupId, originalFilename, fileUrl, 0);
    }

    // 4. 消息框处理：
    processMsgBox.sendFileMsg(fileHtml, toUserId, toGroupId);

    // 5. 好友列表处理
    processFriendList.sending(content, $sendLi);
});

//上传前
$('.myfile').on('filepreupload', function (event, data, previewId, index) {
    console.log("filepreupload");
});

// 绑定发送按钮回车事件
$('#dope').keydown(function (e) {
    if (e.keyCode == 13) {
        $('.sendBtn').trigger('click');
        e.preventDefault(); //屏蔽enter对系统作用。按后增加\r\n等换行
    }
});

// 绑定发送按钮点击事件
$('.sendBtn').on('click', function () {
    console.log("点击发送按钮时间");
    var fromUserId = userId;
    var toUserId = $('#toUserId').val();
    var toGroupId = $('#toGroupId').val();
    var news = $('#dope').val();
    console.log(news);
    if (toUserId == '' && toGroupId == '') {
        alert("请选择对话方");
        return;
    }
    if (news == '') {
        alert('消息不能为空');
        return;
    } else {
        if (toUserId.length != 0) {
            ws.singleSend(fromUserId, toUserId, news);
        } else {
            ws.groupSend(fromUserId, toGroupId, news);
        }

        $('#dope').val('');
        var avatarUrl = $('#avatarUrl').attr("src");
        var msg = '';
        msg += '<li>' +
            '<div class="news">' + news + '</div>' +
            '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
            '</li>';

        // 消息框处理：
        processMsgBox.sendMsg(msg, toUserId, toGroupId)
        // 好友列表处理：
        var $sendLi = $('#conLeft').find('li.bg');
        processFriendList.sending(news, $sendLi);
    }
})

$('.ExP').on('mouseenter', function () {
    $('.emjon').show();
})

$('.emjon').on('mouseleave', function () {
    $('.emjon').hide();
})

$('.emjon li').on('click', function () {
    var imgSrc = $(this).children('img').attr('src');
    $('.emjon').hide();
    var fromUserId = userId;
    var toUserId = $('#toUserId').val();
    var toGroupId = $('#toGroupId').val();
    var content = '<img class="Expr" src="' + imgSrc + '">';
    if (toUserId == '' && toGroupId == '') {
        alert("请选择对话方");
        return;
    }
    if (toUserId.length != 0) {
        ws.singleSend(fromUserId, toUserId, content);
    } else {
        ws.groupSend(fromUserId, toGroupId, content);
    }
    var avatarUrl = $('#avatarUrl').attr("src");
    var msg = '';
    msg += '<li>' +
        '<div class="news">' + content + '</div>' +
        '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
        '</li>';
    processMsgBox.sendMsg(msg, toUserId, toGroupId);
    var $sendLi = $('#conLeft').find('li.bg');
    content = "[图片]";
    processFriendList.sending(content, $sendLi);
})
// 好友框点击事件
function friendLiClickEvent() {

    // 1. 设置点击阴影效果
    $(this).addClass('bg').siblings().removeClass('bg');

    // 2. 设置显示右侧消息框
    $('.main').css("display", "-webkit-box");

    // 3. 设置消息框显示对方信息，清空对方id
    var intername = $(this).children('.liLeft').children('.name').text();
    var toUserId = $(this).children('.liLeft').children('.hidden-userId').text();
    var toGroupId = $(this).children('.liLeft').children('.hidden-groupId').text();
    $('.headName').text(intername);

    $('#toUserId').val("");
    $('#toGroupId').val("");
    // 4. 设置显示已收到的信息，设置好对方的id
    $('.newsList').html('');
    var messageArray;
    //console.log(sentMessageMap);
    if (toUserId.length != 0) {
        messageArray = sentMessageMap.get(toUserId);
        //console.log(sentMessageMap.get(toUserId));
        $('#toUserId').val(toUserId);
    } else {
        messageArray = sentMessageMap.get(toGroupId);
        //console.log(sentMessageMap.get(toGroupId));
        $('#toGroupId').val(toGroupId);
    }
    for (var i = 0; i < messageArray.length; i++) {
        //console.log(messageArray[i]);
        $('.newsList').append(messageArray[i]);
    }

    // 5.设置消息框滚动条滑到底部
    $('.context').scrollTop($('.context')[0].scrollHeight);

    // 6. 去掉红色提醒徽章
    var $badge = $(this).find(".layui-badge");
    if ($badge.length > 0) {
        $badge.remove();
    }
}

// 处理消息框的对象，统一管理相关处理函数，主要包括4个事件函数：
// (实际上应该有8个事件函数，发送得4个：单发普通信息、群发普通信息、单发文件信息、群发文件信息，
// 再加上对应的接收4个，但根据实际情况，发现代码可重用，于是便缩减为4个)
// 1. sendMsg: 发送(单个、群)消息时，调用此函数处理消息框变化；
// 2. sendFileMsg： 文件上传成功后，发送(单个、群)文件消息时，调用此函数处理消息框变化；
// 3. receiveSingleMsg： 收到单发(普通对话、文件)消息时，调用此函数处理消息框变化；
// 4. receiveGroupMsg： 收到群发(普通对话、文件)消息时，调用此函数处理消息框变化。
var processMsgBox = {
    sendMsg: function (msg, toUserId, toGroupId) {
        // 1. 把内容添加到消息框
        $('.newsList').append(msg);

        // 2. 手动计算、调整回显消息的宽度
        var $newsDiv = $('.newsList li').last().children("div").first();
        var fixWidth = 300; // 自定义的消息框本身的最长宽度
        var maxWidth = 493; // 消息框所在行(div)的满宽度(不包含头像框的宽度部分)
        var minMarginLeftWidth = 224; // 按理说应该是 maxwidth - fixWidth，这里出现了点问题
        var marginLeftWidth; // 要计算消息框的margin-left宽度
        if ($newsDiv.actual('width') < fixWidth) {
            marginLeftWidth = maxWidth - $newsDiv.actual('width');

            $newsDiv.css("margin-left", marginLeftWidth + "px");
        } else {
            $newsDiv.css("width", fixWidth + "px")
                .css("margin-left", minMarginLeftWidth + "px");
        }

        // 3. 把 调整后的消息html标签字符串 添加到已发送用户消息表
        if (toUserId!=null&&toUserId.length != 0) {
            sentMessageMap.get(toUserId).push($('.newsList li').last().prop("outerHTML"));

        } else {
            sentMessageMap.get(toGroupId).push($('.newsList li').last().prop("outerHTML"));
        }

        // 4. 滚动条往底部移
        $('.context').scrollTop($('.context')[0].scrollHeight);
    },

    sendFileMsg: function (msg, toUserId, toGroupId) {
        // 注意，文件信息消息框不需要计算宽度，已通过css设置好固定的样式
        // 1. 回显发送的新消息
        $('.newsList').append(msg);

        // 2. 把消息html标签字符串 添加到已发送用户消息表
        if (toUserId!=null&&toUserId.length != 0) {
            sentMessageMap.get(toUserId).push($('.newsList li').last().prop("outerHTML"));

        } else {
            sentMessageMap.get(toGroupId).push($('.newsList li').last().prop("outerHTML"));
        }

        // 3. 消息框往下移
        $('.context').scrollTop($('.context')[0].scrollHeight);
    },

    receiveSingleMsg: function (msg, fromUserId) {
        // 1. 设置消息框可见
        $('.conRight').css("display", "-webkit-box");
        // 2. 把新消息放到暂存区$('.newsList-temp)，如果用户正处于与发出新消息的用户的消息框，则消息要回显
        $('.newsList-temp').append(msg);
        var $focusUserId = $("#conLeft .bg").find('span.hidden-userId');
        if ($focusUserId.length > 0 && $focusUserId.html() == fromUserId) {
            $('.newsList').append(msg);
        }
        // 3. 利用暂存区手动计算、调整新消息的宽度；
        var $answersDiv = $('.newsList-temp li').last().children("div").first();
        var fixWidth = 300; // 消息框本身的最长宽度
        var maxWidth = 480; // 消息框所在行(div)的满宽度(不包含头像框的宽度部分)
        var minMarginRightWidth = 212; // 按理说应该是 maxwidth - fixWidth，这里出现了点问题
        var marginRightWidth; // 要计算消息框的margin-right宽度
        if ($answersDiv.actual('width') < fixWidth) {
            marginRightWidth = maxWidth - $answersDiv.actual('width');
            $answersDiv.css("margin-right", marginRightWidth + "px");
            if ($focusUserId.length > 0 && $focusUserId.html() == fromUserId) {
                $('.newsList li').last().children("div").first()
                    .css("margin-right", marginRightWidth + "px");
            }
        } else {
            $answersDiv.css("width", fixWidth + "px")
                .css("margin-right", minMarginRightWidth + "px");
            if ($focusUserId.length > 0 && $focusUserId.html() == fromUserId) {
                $('.newsList li').last().children("div").first()
                    .css("width", fixWidth + "px")
                    .css("margin-right", minMarginRightWidth + "px");
            }
        }

        // 4. 把 调整后的消息html标签字符串 添加到已发送用户消息表，并清空暂存区
        sentMessageMap.get(fromUserId).push($('.newsList-temp li').last().prop("outerHTML"));
        $('.newsList-temp').empty();

        // 5. 滚动条滑到底
        $('.context').scrollTop($('.context')[0].scrollHeight);
    },

    receiveGroupMsg: function (msg, toGroupId) {
        // 1. 设置消息框可见
        $('.conRight').css("display", "-webkit-box");

        // 2. 把新消息放到暂存区$('.newsList-temp)，如果用户正处于与发出新消息的用户的消息框，则消息要回显
        $('.newsList-temp').append(msg);
        var $focusGroupId = $("#conLeft .bg").find('span.hidden-groupId');
        if ($focusGroupId.length > 0 && $focusGroupId.html() == toGroupId) {
            $('.newsList').append(msg);
        }

        // 3. 手动计算、调整回显消息的宽度
        var $answersDiv = $('.newsList-temp li').last().children("div").first();
        var fixWidth = 300; // 消息框本身的最长宽度
        var maxWidth = 480; // 消息框所在行(div)的满宽度(不包含头像框的宽度部分)
        var minMarginRightWidth = 212; // 按理说应该是 maxwidth - fixWidth，这里出现了点问题
        var marginRightWidth; // 要计算消息框的margin-right宽度
        if ($answersDiv.actual('width') < fixWidth) {
            marginRightWidth = maxWidth - $answersDiv.actual('width');
            $answersDiv.css("margin-right", marginRightWidth + "px");
            if ($focusGroupId.length > 0 && $focusGroupId.html() == toGroupId) {
                $('.newsList li').last().children("div").first()
                    .css("margin-right", marginRightWidth + "px");
            }
        } else {
            $answersDiv.css("width", fixWidth + "px")
                .css("margin-right", minMarginRightWidth + "px");
            if ($focusGroupId.length > 0 && $focusGroupId.html() == toGroupId) {
                $('.newsList li').last().children("div").first()
                    .css("width", fixWidth + "px")
                    .css("margin-right", minMarginRightWidth + "px");
            }
        }

        // 4. 把 调整后的消息html标签字符串 添加到已发送用户消息表，并清空暂存区
        sentMessageMap.get(toGroupId).push($('.newsList-temp li').last().prop("outerHTML"));
        $('.newsList-temp').empty();

        // 5. 滚动条滑到底
        $('.context').scrollTop($('.context')[0].scrollHeight);
    }
}

var processFriendList = {
    sending: function (content, $sendLi) {
        // 1. 设置部分新消息提醒
        if (content.length > 8) {
            content = content.substring(0, 8) + "...";
        }
        $('#conLeft').find('li.bg').children('.liRight').children('.infor').text(content);
        // 2. 如果存在新消息提醒徽章，则去除徽章
        if ($sendLi.find('.layui-badge').length > 0) {
            $sendLi.find('.layui-badge').remove();
        }
        //$('.conLeft ul').prepend('<li class="bg">' + $sendLi.html() + '</li>');
        // 3. 好友框新消息置顶
        $('#conLeft ul').prepend($sendLi.prop("outerHTML"));
        $sendLi.remove();
        $('#conLeft ul li').first().on('click', friendLiClickEvent)
    },

    receiving: function (content, $receiveLi,flag) {
        console.log("修改好友列表");
        // 1. 设置红色提醒

        if(!flag){
            var $badge = $receiveLi.find(".layui-badge");
            if ($badge.length > 0) {
                $badge.html(parseInt($badge.html()) + 1);
            } else {
                var badgeHTML = '<span class="layui-badge badge-avatar">1</span>';
                $receiveLi.children(".liLeft").prepend(badgeHTML);
            }
            // 2. 设置部分新消息提醒
            if (content.length > 2) { // 只显示前八个字符
                content = "\n未读:"+content.substring(0, 3);
            } else {
                content = "\n未读:"+content ;
            }
            if (content.search("<img") != -1) { // 若是图片，显示 “[图片]”
                content = "\n未读:图片";
            }
            $receiveLi.children(".liLeft").children('.infor').text(content);
        }


        // 3. 新消息置顶
        $('#conLeft ul').prepend($receiveLi.prop("outerHTML"));
        $('#conLeft ul li').first().on('click', friendLiClickEvent);
        $receiveLi.remove();
    }
}
// 自定义数据结构：已发送用户消息表
function SentMessageMap() {
    this.elements = new Array();

    //获取MAP元素个数
    this.size = function () {
        return this.elements.length;
    };

    //判断MAP是否为空
    this.isEmpty = function () {
        return (this.elements.length < 1);
    };

    //删除MAP所有元素
    this.clear = function () {
        this.elements = new Array();
    };

    //向MAP中增加元素（key, value)
    this.put = function (_key, _value) {
        this.elements.push({
            key: _key,
            value: _value
        });
    };

    //删除指定KEY的元素，成功返回True，失败返回False
    this.removeByKey = function (_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //删除指定VALUE的元素，成功返回True，失败返回False
    this.removeByValue = function (_value) {//removeByValueAndKey
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //删除指定VALUE的元素，成功返回True，失败返回False
    this.removeByValueAndKey = function (_key, _value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value && this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //获取指定KEY的元素值VALUE，失败返回NULL
    this.get = function (_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return false;
        }
        return false;
    };

    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
    this.element = function (_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return null;
        }
        return this.elements[_index];
    };

    //判断MAP中是否含有指定KEY的元素
    this.containsKey = function (_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //判断MAP中是否含有指定VALUE的元素
    this.containsValue = function (_value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //判断MAP中是否含有指定VALUE的元素
    this.containsObj = function (_key, _value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value && this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    };

    //获取MAP中所有VALUE的数组（ARRAY）
    this.values = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    };

    //获取MAP中所有VALUE的数组（ARRAY）
    this.valuesByKey = function (_key) {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            if (this.elements[i].key == _key) {
                arr.push(this.elements[i].value);
            }
        }
        return arr;
    };

    //获取MAP中所有KEY的数组（ARRAY）
    this.keys = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    };

    //获取key通过value
    this.keysByValue = function (_value) {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            if (_value == this.elements[i].value) {
                arr.push(this.elements[i].key);
            }
        }
        return arr;
    };

    //获取MAP中所有KEY的数组（ARRAY）
    this.keysRemoveDuplicate = function () {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            var flag = true;
            for (var j = 0; j < arr.length; j++) {
                if (arr[j] == this.elements[i].key) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                arr.push(this.elements[i].key);
            }
        }
        return arr;
    };
}
