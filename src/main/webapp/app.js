function validate() {
    const desc = $('#description').val();
    if (desc === '') {
        alert('Please enter the task!');
        return false;
    }
}

$(document).ready(getItems(0))

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