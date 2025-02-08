import React, { useState, useEffect } from "react";
import axios from "axios";

const SuperAdminUserTable = () => {
    const [userList, setUserList] = useState([]);
    const token = localStorage.getItem('token');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [newRole, setNewRole] = useState('');
    const fetchAllUsers = async () => {
        try {
            const url = 'http://localhost:8080/superadmin/user/getall';
            const response = await axios.get(url, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setUserList(response.data);
            console.log('User data', response.data);
        } catch (err) {
            console.error('Error fetching user data:', err);
        }
    };

    useEffect(() => {
        fetchAllUsers();
    }, [token]);

    const [search, setSearch] = useState({
        fullName: "",
        time: "",
        email: "",
        phone: "",
        role: "all",
        isActive: "all",
        isEmailActive: "all",
    });

    const usersPerPage = 10;
    const [currentPage, setCurrentPage] = useState(1);

    const formatTime = (createdAt) => {
        if (!createdAt) return "";
        const postDate = new Date(createdAt);
        return postDate.toLocaleDateString("vi-VN", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        });
    }

    const indexOfLastUser = currentPage * usersPerPage;
    const indexOfFirstUser = indexOfLastUser - usersPerPage;

    const filteredUsers = userList.filter((item) => {
        const matchesFullName = item?.fullName.toLowerCase().includes(search.fullName.toLowerCase()) || search.fullName === "";
        const matchesEmail = item?.email.toLowerCase().includes(search.email.toLowerCase()) || search.email === "";
        const matchesPhone = item?.phone.toLowerCase().includes(search.phone.toLowerCase()) || search.phone === "";
        const matchesRole = 
        search.role === "all" ||
        (search.role === "USER" && item?.roleId.name === "USER") ||
        (search.role === "ADMIN" && item?.roleId.name === "ADMIN") ||
        (search.role === "SUPER_ADMIN" && item?.roleId.name === "SUPER_ADMIN");
        const matchesActive = search.isActive === "all" || (search.isActive === "true" ? item?.active === true : item?.active === false);
        const matchesEmailActive = search.isEmailActive === "all" || (search.isEmailActive === "true" ? item?.emailActive === true : item?.emailActive === false);
        const matchesTime = (item?.createdAt && String(formatTime(item.createdAt)).toLowerCase().includes(search.time.toLowerCase())) || search.time === "";

        return matchesFullName && matchesEmail && matchesPhone && matchesRole && matchesActive && matchesEmailActive && matchesTime;
    });

    const currentUsers = filteredUsers.slice(indexOfFirstUser, indexOfLastUser);

    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(filteredUsers.length / usersPerPage); i++) {
        pageNumbers.push(i);
    }

    const handleSearchChange = (e) => {
        const { name, value } = e.target;
        setSearch((prevSearch) => ({
            ...prevSearch,
            [name]: value,
        }));
    };
    const handleLockUser = async (id) => {
        try {
            await axios.post(`http://localhost:8080/superadmin/user/lock/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert("Khóa tài khoản thành công");
            fetchAllUsers();
        } catch (error) {
            alert(error.response.data.error);
        }
    };
    const handleUnLockUser = async (id) => {
        try {
            await axios.post(`http://localhost:8080/superadmin/user/unlock/${id}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert("Mở khóa tài khoản thành công");
            fetchAllUsers();
        } catch (error) {
            alert(error.response.data.error);
        }
    };
    const handleUpdateRole = async () => {
        try {
            await axios.post(`http://localhost:8080/superadmin/${selectedUser?.userId}/update/role/${newRole}`, {}, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            alert("Thay đổi quyền hạn tài khoản thành công");
            setIsModalOpen(false);
            fetchAllUsers();
          
        } catch (error) {
            alert(error.response.data.error);
        }
    };

    const handleOpenModal = (user) => {
        setSelectedUser(user);
        setNewRole(user?.roleId.id);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setSelectedUser(null);
    };
    return (
        <div className="super-admin-user-table">
            <div className="filter-container">
                <input type="text" name="fullName" placeholder="Search by fullName" value={search.fullName} onChange={handleSearchChange} />
                <input type="text" name="email" placeholder="Search by email" value={search.email} onChange={handleSearchChange} />
                <input type="text" name="phone" placeholder="Search by phone" value={search.phone} onChange={handleSearchChange} />
                <input type="text" name="time" placeholder="Search by time" value={search.time} onChange={handleSearchChange} />
                <select name="role" value={search.role} onChange={handleSearchChange}>
                    <option value="all">All</option>
                    <option value="USER">USER</option>
                    <option value="ADMIN">ADMIN</option>
                    <option value="SUPER_ADMIN">SUPER ADMIN</option>
                </select>
                <select name="isActive" value={search.isActive} onChange={handleSearchChange}>
                    <option value="all">All active</option>
                    <option value="true">Active</option>
                    <option value="false">Not Active</option>
                </select>
                <select name="isEmailActive" value={search.isEmailActive} onChange={handleSearchChange}>
                    <option value="all">All email verify</option>
                    <option value="true">Verified</option>
                    <option value="false">Not verify</option>
                </select>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Create At</th>
                        <th>Role</th>
                        <th>Active</th>
                        <th>Email Verify</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {currentUsers.map((user, index) => (
                        <tr key={user?.userId}>
                            <td>{indexOfFirstUser + index + 1}</td>
                            <td>{user.fullName}</td>
                            <td>{user.email}</td>
                            <td>{user.phone}</td>
                            <td>{formatTime(user.createdAt)}</td>
                            <td>{user.roleId.name}</td>
                            <td>{user.active ? "Active" : "Inactive"}</td>
                            <td>{user.emailActive ? "Verified" : "Not Verified"}</td>
                            <td className="super-admin-action-user">
                            
                                <button className="hide-btn pagination-btn"
                                 disabled={user.roleId.name === "SUPER_ADMIN"}
                                onClick={() => handleOpenModal(user)}>
                                    <i class="fa-solid fa-shield-halved"></i>
                                </button>

                                {user.active ? (
                                    <button className="hide-btn pagination-btn" 
                                    disabled={user.roleId.name === "SUPER_ADMIN"}
                                    onClick={() => handleLockUser(user?.userId)}>
                                        <i class="fa-solid fa-lock"></i>
                                    </button>
                                ) : (
                                    <button className="hide-btn pagination-btn"
                                    disabled={user.roleId.name === "SUPER_ADMIN"}
                                    onClick={() => handleUnLockUser(user?.userId)}>
                                        <i class="fa-solid fa-unlock"></i>
                                    </button>
                                )}



                            </td>
                        </tr>
                    ))}
                </tbody>
                
            </table>
            {isModalOpen && (
               <div className="modal-overlay">
               <div className="modal">
                   <div className="modal-header">
                       <h2>Phân quyền </h2>
                       <br/>
                       <label>{selectedUser?.fullName}</label>
                       <button onClick={handleCloseModal} className="close-btn">
                           <i className="fa-solid fa-xmark"></i>
                       </button>
                   </div>
                   <div className="modal-body">
                       <select className="role-select" value={newRole} onChange={(e) => setNewRole(e.target.value)}>
                           <option value="2" >USER</option>
                           <option value="1" >ADMIN</option>
                           <option value="3" >SUPER ADMIN</option>
                       </select>
                   </div>
                       <button className="save-btn" onClick={() => handleUpdateRole()}>Lưu</button>
               </div>
           </div>
           
            )}

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
};

export default SuperAdminUserTable;