function validate() {
    const desc = $('#description').val();
    if (desc === '') {
        alert('Please enter the task!');
        return false;
    }
}

$(document).ready(function() {
    getItems(0);
    login();
});

function getItems(num) {
    $.ajax({
        type: 'GET',
        contentType: 'json',
        data: {json:num},
        url: 'http://localhost:8080/todo/tasks',
    }).done(function(data) {
        for (let i = 0; i < data.length; i++) {
            const d = new Date(data[i].created);
            const date = d.toLocaleTimeString() + ' ' + d.toDateString();
            $('#table tr:last').after('<tr class=\"item\"><td>' + data[i].description + '</td>');
            $('#table td:last').after('<td>' + date + '</td>');
            const user = JSON.stringify(data[i].user);
            $('#table td:last').after('<td>' + user.split('\"')[5] + '</td>');
            if (data[i].done === 0) {
                $('#table td:last').after(
                    '<td><input type=\"checkbox\" value=\"'
                    + data[i].id
                    + '\" onchange=\"check(this)\">'
                    + '</td></tr>'
                );
            } else {
                $('#table td:last').after(
                    '<td><input type=\"checkbox\" checked value=\"'
                    + data[i].id
                    + '\" onchange=\"check(this)\">' + '</td></tr>'
                );
            }
        }
    });
}

function login() {
    $.ajax({
        type: 'GET',
        dataType: 'html',
        url: 'http://localhost:8080/todo/login',
    }).always(function(data) {
        $('#sign').html(data);
    });
}

function check(checkbox) {
    if ($(checkbox).is(":checked")) {
        $.ajax({
            type: 'GET',
            contentType: 'json',
            data: {json:[1, checkbox.value]},
            url: 'http://localhost:8080/todo/check',
        }).done(function() {
            let elements = document.querySelectorAll('tr.item');
            elements.forEach(e => e.remove());
            getItems(0);
        });
    } else {
        $.ajax({
            type: 'GET',
            contentType: 'json',
            data: {json:[0, checkbox.value]},
            url: 'http://localhost:8080/todo/check',
        }).done();
    }
}

function getCompletedItems(checkbox) {
    let elements = document.querySelectorAll('tr.item');
    elements.forEach(e => e.remove());
    if ($(checkbox).is(":checked")) {
        getItems(1);
    } else {
        getItems(0);
    }
}

function validateReg() {
    const name = $('#name').val();
    const email = $('#email').val();
    const pass = $('#password').val();
    if (name === '' || email === '' || pass === '') {
        alert('All fields are required!');
        return false;
    }
}

function validateLogin() {
    const email = $('#email').val();
    const pass = $('#password').val();
    if (email === '' || pass === '') {
        alert('All fields are required!');
        return false;
    }
}