import React, { useState, useEffect } from "react";
import "./../styles/AdminPostContent.css";
import axios from "axios";
function AdminGroupContent() {
    // Dữ liệu mẫu về bài viết
    const [groups, setGroups] = useState([]);
    const token = localStorage.getItem('token');
    const fetchAllGroup = async () => {
        try {
            const url = 'http://localhost:8080/admin/dashboard/group/getall'; // Hiển thị tất cả

            const response = await axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${token}` // Thêm token vào header
                }
            });
            setGroups(response.data);
            console.log('postList', response.data);
        } catch (err) {
            console.error('Error fetching user data:', err);
        }
    };
    useEffect(() => {
        fetchAllGroup();
    }, [token]);
    const [search, setSearch] = useState({
        name: "",
        time: "",
        hidden: "all",
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
    const filteredGroups = groups.filter((group) => {
        const matchesUser =
            group?.name.toLowerCase().includes(search.name.toLowerCase()) || search.name === "";
        const matchesTime =
            (group?.createdAt &&
                String(formatTime(group.createdAt)).toLowerCase().includes(search.time.toLowerCase())) ||
            search.time === "";
        const matchesHidden =
            search.hidden === "all" ||
            (search.hidden === "true" ? group?.deleted === true : group?.deleted === false);


        return matchesUser && matchesTime && matchesHidden;
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
    const handledeleteGroup = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/groups/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert("Khóa nhóm thành công")
            fetchAllGroup();
        } catch (error) {
            alert(error.response.data.error);
        }
    };
    const handleRestoreGroup = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/groups/restore/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert(" hiển thị nhóm thành công")
            fetchAllGroup();
        } catch (error) {
            alert(error.response.data.error);
        }
    };
    return (
        <div className="admin-content">
            <h3>Group List</h3>

            {/* Bộ lọc */}
            <div className="filter-container">
                <input
                    type="text"
                    name="name"
                    placeholder="Search by name"
                    value={search.name}
                    onChange={handleSearchChange}
                />
                <input
                    type="text"
                    name="time"
                    placeholder="Search by time"
                    value={search.time}
                    onChange={handleSearchChange}
                />
                <select
                    name="hidden"
                    value={search.hidden}
                    onChange={handleSearchChange}
                >
                    <option value="all">All</option>
                    <option value="true">Hidden</option>
                    <option value="false">Visible</option>
                </select>

            </div>

            {/* Bảng bài viết */}
            <table className="posts-table">
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Name</th>
                        <th>Time</th>
                        <th>Status</th>
                        <th>view</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {currentGroups.map((item, index) => (
                        <tr key={item?.groupId}>
                            <td>{indexOfFirstGroup + index + 1}</td>
                            <td>{item.name}</td>

                            <td>{formatTime(item?.createdAt)}</td>
                            <td>{item?.deleted === false ? 'Đang hoạt động' : 'Đã xóa'}</td>
                            <td>
                                <button className="hide-btn" onClick={() => openNewPage(`http://localhost:3000/group/${item?.groupId}`)}>
                                    xem
                                </button>

                            </td>
                            <td>
                                {item?.deleted === true ? (

                                    <button className="hide-btn" onClick={() => handleRestoreGroup(item?.groupId)}>
                                        <i class="fa-solid fa-rotate-left"></i>
                                    </button>
                                ) : (

                                    <button className="hide-btn" onClick={() => handledeleteGroup(item?.groupId)}>
                                        <i class="fa-solid fa-ban"></i>
                                    </button>
                                )}

                            </td>
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

export default AdminGroupContent;
