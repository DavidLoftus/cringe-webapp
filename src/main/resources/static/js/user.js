function updateRole(id, isRoot, isAdmin) {
    // Get vals
    rootVal = document.getElementById('user_isRoot_' + id).checked;
    adminVal = document.getElementById('user_isAdmin_' + id).checked;

    console.log("Root: " + isRoot + " == " + rootVal);
    console.log("Admin: " + isAdmin + " == " + adminVal);

    if(isRoot != rootVal) {
        fetch('/root/changeRole', {
            method: 'POST',
            body: new URLSearchParams({
                id: id,
                role: 'root',
            })
        });
    }

    if(isAdmin != adminVal) {
        fetch('/root/changeRole', {
            method: 'POST',
            body: new URLSearchParams({
                id: id,
                role: 'admin',
            })
        });
    }
}