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

function refundOrder(id) {
    if (confirm("Are you sure that you want to refund this order?")) {
        console.log("Refund requested");
        fetch('/admin/refundOrder', {
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