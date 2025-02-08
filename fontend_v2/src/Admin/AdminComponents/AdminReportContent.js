import React, { useState, useEffect } from "react";
import "./../styles/AdminPostContent.css";
import axios from "axios";
function AdminReportContent() {
    // Dữ liệu mẫu về bài viết
    const [reports, setReports] = useState([]);
    const token = localStorage.getItem('token');
    const fetchAllGroup = async () => {
        try {
            const url = 'http://localhost:8080/report/get-all'; // Hiển thị tất cả

            const response = await axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${token}` // Thêm token vào header
                }
            });
            setReports(response.data);
            console.log('postList', response.data);
        } catch (err) {
            console.error('Error fetching user data:', err);
        }
    };
    useEffect(() => {
        fetchAllGroup();
    }, [token]);
    const [search, setSearch] = useState({
        reportByName: "",
        time: "",
        user: "",
    });

    const groupsPerPage = 10; // Số bài viết hiển thị trên mỗi trang
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const formatTime = (createdAt) => {
        if (!createdAt) return ""; // Xử lý trường hợp null hoặc undefined
        const postDate = new Date(createdAt);
        return postDate.toLocaleDateString("vi-VN", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        });
    }
    // Tính toán các chỉ số bài viết hiển thị theo trang
    const indexOfLastGroups = currentPage * groupsPerPage;
    const indexOfFirstGroup = indexOfLastGroups - groupsPerPage;

    // Lọc bài viết theo tiêu chí
    const filteredGroups = reports.filter((item) => {
        const matchesUser =
        item?.reportedBy?.fullName.toLowerCase().includes(search.reportByName.toLowerCase()) || search.reportByName === "";
        const matchesTime =
            (item?.createdAt &&
                String(formatTime(item.createdAt)).toLowerCase().includes(search.time.toLowerCase())) ||
            search.time === "";
        const matchesUserReport =
        item?.user?.fullName.toLowerCase().includes(search.user.toLowerCase()) || search.user === "";


        return matchesUser && matchesTime && matchesUserReport;
    });

    const currentGroups = filteredGroups.slice(indexOfFirstGroup, indexOfLastGroups);

    // Hàm xử lý thay đổi trang
    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    // Tính toán tổng số trang
    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(filteredGroups.length / groupsPerPage); i++) {
        pageNumbers.push(i);
    }

    // Xử lý thay đổi giá trị bộ lọc
    const handleSearchChange = (e) => {
        const { name, value } = e.target;
        setSearch((prevSearch) => ({
            ...prevSearch,
            [name]: value,
        }));
    };

    const openNewPage = (url) => {
        if (!url) return;
        window.open(url, "_blank"); // Mở tab mới
    };
   
    return (
        <div className="admin-content">
            <h3>Report List</h3>

            {/* Bộ lọc */}
            <div className="filter-container">
                <input
                    type="text"
                    name="reportByName"
                    placeholder="Search by reporter name"
                    value={search.reportByName}
                    onChange={handleSearchChange}
                />
                <input
                    type="text"
                    name="time"
                    placeholder="Search by time"
                    value={search.time}
                    onChange={handleSearchChange}
                />
                   <input
                    type="text"
                    name="user"
                    placeholder="Search by user name"
                    value={search.user}
                    onChange={handleSearchChange}
                />
               

            </div>

            {/* Bảng bài viết */}
            <table className="posts-table">
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>reporter</th>
                        <th>user</th>
                        <th>Time</th>
                        <th>reason</th>
                    </tr>
                </thead>
                <tbody>
                    {currentGroups.map((item, index) => (
                        <tr key={item?.reportId}>
                            <td>{indexOfFirstGroup + index + 1}</td>
                            <td onClick={() => openNewPage(`http://localhost:3000/friend/${item?.reportedBy?.userId}`)}>{item?.reportedBy?.fullName}</td>
                            <td onClick={() => openNewPage(`http://localhost:3000/friend/${item?.user?.userId}`)}>{item?.user?.fullName}</td>
                            <td>{formatTime(item?.createdAt)}</td>
                            <td>{item?.reason}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Phân trang */}
            <div className="pagination-container">
                <ul className="pagination">
                    <li>
                        <button
                            onClick={() => paginate(currentPage - 1)}
                            disabled={currentPage === 1}
                            className="pagination-btn"
                        >
                            <i class="fa-solid fa-arrow-left"></i>
                        </button>
                    </li>
                    {pageNumbers.map(number => (
                        <li key={number}>
                            <button
                                onClick={() => paginate(number)}
                                className={`pagination-btn ${currentPage === number ? 'active' : ''}`}
                            >
                                {number}
                            </button>
                        </li>
                    ))}
                    <li>
                        <button
                            onClick={() => paginate(currentPage + 1)}
                            disabled={currentPage === pageNumbers.length}
                            className="pagination-btn"
                        >
                            <i class="fa-solid fa-arrow-right"></i>
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    );
}

export default AdminReportContent;
