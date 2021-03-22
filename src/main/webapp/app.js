function validate() {
    const desc = $('#description').val();
    const select = document.getElementById('category');
    if ((desc === '') || (!select.value)) {
        alert('Please enter the task and choose category!');
        return false;
    }
    return true;
}

$(document).ready(function() {
    getCategories();
    getItems(0);
    login();
});

function getCategories() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/todo/categories',
    }).done(function(data) {
        let options = '';
        for (let item of data) {
            options += '<option value=\"' + item.id + '\">' + item.name + '</option>';
        }
        $('#options').html('<label><select id="category" name="category" multiple>' + options + '</select></label>');
        $('#category').multiselect();
    });
}

function getItems(num) {
    $.ajax({
        type: 'GET',
        contentType: 'json',
        data: {json:num},
        url: 'http://localhost:8080/todo/tasks',
    }).done(function(data) {
        for (let item of data) {
            const d = new Date(item.created);
            const date = d.toLocaleTimeString() + ' ' + d.toDateString();
            $('#table tr:last').after('<tr class=\"item\"><td>' + item.description + '</td>');
            $('#table td:last').after('<td>' + date + '</td>');
            let category = item.categories[0].name;
            for (let i = 1; i < item.categories.length; i++) {
                category += ', ' + item.categories[i].name
            }
            $('#table td:last').after('<td>' + category + '</td>');
            $('#table td:last').after('<td>' + item.user.name + '</td>');
            if (item.done === 0) {
                $('#table td:last').after(
                    '<td><input type=\"checkbox\" value=\"'
                    + item.id
                    + '\" onchange=\"check(this)\">'
                    + '</td></tr>'
                );
            } else {
                $('#table td:last').after(
                    '<td><input type=\"checkbox\" checked value=\"'
                    + item.id
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
    }).done(function(data) {
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
    return true;
}

function validateLogin() {
    const email = $('#email').val();
    const pass = $('#password').val();
    if (email === '' || pass === '') {
        alert('All fields are required!');
        return false;
    }
    return true;
}

function signOut() {
    $.ajax({
        url: 'http://localhost:8080/todo/reg',
    }).done(
        window.location.href = "http://localhost:8080/todo/login.html"
    )
}

function addTask() {
    if (validate() === true) {
        const description = $('#description').val();
        const category = $('#category').val();
        const json = {description: description, category: category};
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(json),
            url: 'http://localhost:8080/todo/tasks',
        }).done(
            window.location.href = "http://localhost:8080/todo/index.html"
        )
    }
}

function reg() {
    if (validateReg() === true) {
        const name = $('#name').val();
        const mail = $('#email').val();
        const pass = $('#password').val();
        const json = {name: name, email: mail, password: pass};
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(json),
            url: 'http://localhost:8080/todo/reg',
        }).done(
            window.location.href = "http://localhost:8080/todo/login.html"
        )
    }
}

function log() {
    if (validateLogin() === true) {
        const mail = $('#email').val();
        const pass = $('#password').val();
        const json = {email: mail, password: pass};
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(json),
            url: 'http://localhost:8080/todo/login',
            statusCode: {
                200: function () {
                    window.location.href = "http://localhost:8080/todo/index.html";
                },
                417: function () {
                    alert('E-mail or password incorrect!');
                }
            }
        })
    }
}