"use strict";
let subs = document.getElementsByClassName('submit-data');
$(subs[0]).click(function () {
    let error = document.getElementsByClassName('error')[0];
    let uap = document.getElementById('login').getElementsByTagName('input');
    if (uap[0].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '用户名不能为空！'
    } else if (uap[1].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '密码不能为空！'
    } else {
        $.post('/login', {
            account: uap[0].value,
            password: uap[1].value
        }).done(function (data) {
            if (data.state === 'fail') {
                error.style.display = 'block';
                error.style.color = "red";
                error.innerText = data.wrong + '!';
            } else {
                document.cookie = 'token=' + data.token;
                location.assign('/home');
            }
        });
    }
});
$(subs[1]).click(function () {
        let error = document.getElementsByClassName('error')[0];
        let uap = document.getElementById('regin').getElementsByTagName('input');
        if (uap[0].value === '') {
            error.style.display = 'block';
            error.style.color = "red";
            error.innerText = '账号不能为空！'
        } else {
            error.style.display = 'block';
            error.style.color = "#0f88eb";
            error.innerText = '请稍等。。。';
            $.post('/mesvalidate', {
                mailbox: uap[0].value
            }).done(function (data) {
                if (data.state === 'fail') {
                    error.style.display = 'block';
                    error.style.color = "red";
                    error.innerText = '邮箱已存在！';
                } else {
                    error.style.display = 'block';
                    error.style.color = "#0f88eb";
                    error.innerText = '请去邮箱查收！';
                }
            });
        }
    }
);
$(subs[2]).click(function () {
    let error = document.getElementsByClassName('error')[0];
    let uap = document.getElementById('regin').getElementsByTagName('input');
    if (uap[0].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '账号不能为空！'
    } else if (uap[1].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '昵称不能为空！'
    } else if (uap[2].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '密码不能为空！'
    } else if (uap[3].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '请确认密码！'
    } else if (uap[3].value !== uap[2].value) {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '密码不一致，请重新输入！'
    } else if (uap[4].value === '') {
        error.style.display = 'block';
        error.style.color = "red";
        error.innerText = '请输入验证码！'
    } else {
        $.post('/reg', {
            account: uap[0].value,
            nickname: uap[1].value,
            password: uap[2].value,
            validate: uap[4].value
        }).done(function (data) {
            if (data.state === 'fail') {
                error.style.display = 'block';
                error.style.color = "red";
                error.innerText = data.wrong + '!';
            } else {
                error.style.display = 'block';
                error.style.color = "#0f88eb";
                error.innerText = '注册成功！请登录！'
            }
        });
    }
});
