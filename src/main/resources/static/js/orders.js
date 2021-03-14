function refund(id) {
    if (confirm("Are you sure that you want to refund this order?")) {
        console.log("Refund requested");
        fetch('/orders/refund', {
            method: 'POST',
            body: new URLSearchParams({
                id: id,
            })
        }).then(resp => {
            if (resp.ok) {
                var status = document.getElementById('order_status_' + id);
                status.innerText = "refunded";
            }
        });
    } else {
        console.log("Cancelled refund");
    }
}

function updateOrder(id) {
    var statusSelector = document.getElementById("order_status_" + id);
    var options = statusSelector.options;
    var selected_option = options[options.selectedIndex].text;
    if (confirm("Are you sure that you want to update this order?")) {
        console.log("Update requested");
        fetch('/orders/updateOrder', {
            method: 'POST',
            body: new URLSearchParams({
                id: id,
                status: selected_option.toString(),
            })
        });
    } else {
        console.log("Cancelled order update");
    }
}