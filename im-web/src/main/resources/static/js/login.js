function login() {
    var param={
        "userName": $("#userName").val(),
        "password": $("#password").val()
    };
    $.ajax({
        type: "POST",
        url: "login",
        data: JSON.stringify(param),
        contentType:"application/json;charsetset=UTF-8", //必要 不加报 415
        dataType: "json",//必要 这个没测出来不加有什么影响
        async: false,
        success: function (data) {
            if (data.success) {
                //alert("转到聊天室界面");
                console.log(data.result);
                window.location.href = "chatroom";
                //console.log("转到聊天室界面");
            } else {
                alert(data.msg);
            }
        }
    });
}

function getPassword() {
    //console.info($("#username").val());
    if( $("#username").val()==""){
        alert("请在用户名处输入");
        return ;
    }
    $.ajax({
        type: 'POST',
        url: 'sendPasswordMail',
        dataType: 'json',
        contentType:"application/json;charsetset=UTF-8", //必要
        dataType:"json",//必要
        data: {
            username: $("#username").val(),
        },
        async: false,
        success: function (data) {
            if (data.status == 200) {
                //alert("转到聊天室界面");
                alert("密码已发送至你邮箱");
                //console.log("转到聊天室界面");
            } else {
                alert(data.msg);
            }
        }
    });
}