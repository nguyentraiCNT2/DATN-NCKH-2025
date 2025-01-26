async function includeHTML() {
    const headerContainer = document.getElementById('post-container');
    try {
        const response = await fetch('postItem.html');
        if (!response.ok) throw new Error('Tệp postItem.html không thể tải được');
        const html = await response.text();
        headerContainer.innerHTML = html;
    } catch (error) {
        console.error('Lỗi khi tải tệp postItem.html:', error);
    }
}

// Gọi hàm khi trang đã tải xong
window.onload = includeHTML;
