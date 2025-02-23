import axios from "axios";
import React, { useEffect, useState } from "react";
import "./../styles/AdminPostContent.css";
function AdminListPostByCommentContent() {
    // Dữ liệu mẫu về bài viết
    const [posts, setPosts] = useState([]);
    const token = localStorage.getItem('token');
    const fetchAllPost = async () => {
        try {
            const url = 'http://localhost:8080/admin/posts/get-all-order-by-total-comment'; // Hiển thị tất cả

            const response = await axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${token}` // Thêm token vào header
                }
            });
            setPosts(response.data);
            console.log('postList', response.data);
        } catch (err) {
            console.error('Error fetching user data:', err);
        }
    };
    useEffect(() => {
        fetchAllPost();
    }, [token]);
    const [search, setSearch] = useState({
        user: "",
        group: "",
        time: "",
        hidden: "all",
    });

    const postsPerPage = 10; // Số bài viết hiển thị trên mỗi trang
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
    const indexOfLastPost = currentPage * postsPerPage;
    const indexOfFirstPost = indexOfLastPost - postsPerPage;

    // Lọc bài viết theo tiêu chí
    const filteredPosts = posts.filter((post) => {
        const matchesUser =
            post?.user?.fullName.toLowerCase().includes(search.user.toLowerCase()) || search.user === "";
        const matchesGroup =
            post?.groupId?.name.toLowerCase().includes(search.group.toLowerCase()) || search.group === "";
        const matchesTime =
            (post?.createdAt &&
                String(formatTime(post.createdAt)).toLowerCase().includes(search.time.toLowerCase())) ||
            search.time === "";
        const matchesHidden =
            search.hidden === "all" ||
            (search.hidden === "true" ? post?.deleted === true : post?.deleted === false);


        return matchesUser && matchesGroup && matchesTime && matchesHidden;
    });

    const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

    // Hàm xử lý thay đổi trang
    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    const handleHidePost = (postId) => {
        setPosts(posts.map(post =>
            post.id === postId ? { ...post, hidden: true } : post
        ));
    };

    // Tính toán tổng số trang
    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(filteredPosts.length / postsPerPage); i++) {
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
    const handleHidenPost = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/post/delete/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert("Tắt hiển thị bài viết thành công")
            fetchAllPost();
        } catch (error) {
            alert(error.response.data.error);
        }
    };
    const handleShowPost = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/post/show/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert(" hiển thị bài viết thành công")
            fetchAllPost();
        } catch (error) {
            alert(error.response.data.error);
        }
    };
    const openNewPage = (url) => {
        if (!url) return;
        window.open(url, "_blank"); // Mở tab mới
    };
    const linkpage = (url) =>{
        window.location.href = url;
    }
    const formatLikes = (likes) => {
        if (likes >= 1_000_000_000) {
          return (likes / 1_000_000_000).toFixed(1).replace(/\.0$/, '') + 'B';
        }
        if (likes >= 1_000_000) {
          return (likes / 1_000_000).toFixed(1).replace(/\.0$/, '') + 'M';
        }
        if (likes >= 1_000) {
          return (likes / 1_000).toFixed(1).replace(/\.0$/, '') + 'K';
        }
        return likes;
      };
    return (
        <div className="admin-content">
            <h3>Posts List</h3>

            {/* Bộ lọc */}
            <div className="filter-container">
                <input
                    type="text"
                    name="user"
                    placeholder="Search by user"
                    value={search.user}
                    onChange={handleSearchChange}
                />
                <input
                    type="text"
                    name="group"
                    placeholder="Search by group"
                    value={search.group}
                    onChange={handleSearchChange}
                />
                <input
                    type="text"
                    name="time"
                    placeholder="Search by time"
                    value={search.time}
                    onChange={handleSearchChange}
                />
                {/* <select
                    name="hidden"
                    value={search.hidden}
                    onChange={handleSearchChange}
                >
                    <option value="all">All</option>
                    <option value="true">Hidden</option>
                    <option value="false">Visible</option>
                </select> */}

            </div>

            {/* Bảng bài viết */}
            <table className="posts-table">
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Content</th>
                        <th>User</th>
                        <th>Like</th>
                        <th>Comment</th>
                        <th>Group</th>
                        <th>Time</th>
                        <th>view</th>
                    </tr>
                </thead>
                <tbody>
                    {currentPosts.map((post, index) => (
                        <tr key={post?.postId}>
                            <td>{indexOfFirstPost + index + 1}</td>
                            <td>{post.content}</td>
                            <td>{post?.user?.fullName}</td>
                            <td> {formatLikes(post.totalLike)} lượt thích</td>
                            <td>{formatLikes(post?.totalComment ? post?.totalComment : 0)} bình luận</td>
                            <td>{post?.groupId?.name}</td>
                            <td>{formatTime(post?.createdAt)}</td>
                            <td>
                                <button className="hide-btn" onClick={() => openNewPage(`http://localhost:3000/post/${post?.postId}`)}>
                                    xem
                                </button>
                            </td>
                           
                            {/* <td>
                                {post?.deleted === true ? (

                                    <button
                                        className="hide-btn"

                                        onClick={() => handleShowPost(post?.postId)}>
                                   <i class="fa-solid fa-eye"></i>
                                    </button>
                                ) : (

                                    <button
                                        className="hide-btn"
                                        onClick={() => handleHidenPost(post?.postId)}>
                                      <i class="fa-solid fa-eye-slash"></i>
                                    </button>
                                )}

                            </td> */}
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

export default AdminListPostByCommentContent;
