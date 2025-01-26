async function includeHTML() {
    const headerContainer = document.getElementById('header-container');
    try {
        const response = await fetch('header.html');
        if (!response.ok) throw new Error('Tệp header.html không thể tải được');
        const html = await response.text();
        headerContainer.innerHTML = html;
    } catch (error) {
        console.error('Lỗi khi tải tệp header.html:', error);
    }
}

// Gọi hàm khi trang đã tải xong
window.onload = includeHTML;
