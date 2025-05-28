import axios from "axios";
import { useEffect, useRef, useState } from "react";
import "./../styles/AdminThemeContent.css";

const AdminThemeContent = () => {
  const [themes, setThemes] = useState([]);
  const token = localStorage.getItem("token");
  const [search, setSearch] = useState({
    name: "",
    time: "",
  });
  const [showModal, setShowModal] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [currentTheme, setCurrentTheme] = useState({
    id: null,
    name: "",
    color: "#000000",
    description: "",
    image: null,
  });
  const [imageFile, setImageFile] = useState(null);
  const fileInputRef = useRef(null);

  // Fetch all themes
  const fetchAllThemes = async () => {
    try {
      const response = await axios.get("http://localhost:8080/theme/getAll", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setThemes(response.data);
      console.log("themeList:", response.data);
    } catch (err) {
      console.error("Error fetching themes:", err);
      alert("Lỗi khi tải danh sách theme");
    }
  };

  useEffect(() => {
    fetchAllThemes();
  }, [token]);

  // Pagination
  const themesPerPage = 8;
  const [currentPage, setCurrentPage] = useState(1);
  const indexOfLastTheme = currentPage * themesPerPage;
  const indexOfFirstTheme = indexOfLastTheme - themesPerPage;

  // Format date
  const formatTime = (createdAt) => {
    if (!createdAt) return "";
    const themeDate = new Date(createdAt);
    return themeDate.toLocaleDateString("vi-VN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    });
  };

  // Filter themes
  const filteredThemes = themes.filter((theme) => {
    const matchesName =
      theme?.name?.toLowerCase().includes(search.name.toLowerCase()) ||
      search.name === "";
    const matchesTime =
      (theme?.createdAt &&
        String(formatTime(theme.createdAt))
          .toLowerCase()
          .includes(search.time.toLowerCase())) ||
      search.time === "";
    return matchesName && matchesTime;
  });

  const currentThemes = filteredThemes.slice(indexOfFirstTheme, indexOfLastTheme);

  // Pagination handler
  const paginate = (pageNumber) => setCurrentPage(pageNumber);
  const pageNumbers = [];
  for (let i = 1; i <= Math.ceil(filteredThemes.length / themesPerPage); i++) {
    pageNumbers.push(i);
  }

  // Search handler
  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearch((prevSearch) => ({
      ...prevSearch,
      [name]: value,
    }));
  };

  // Modal handlers
  const openAddModal = () => {
    setIsEditing(false);
    setCurrentTheme({ id: null, name: "", color: "#000000", description: "", image: null });
    setImageFile(null);
    if (fileInputRef.current) fileInputRef.current.value = null;
    setShowModal(true);
  };

  const openEditModal = (theme) => {
    setIsEditing(true);
    setCurrentTheme({
      id: theme.id,
      name: theme.name,
      color: theme.color || "#000000",
      description: theme.description || "",
      image: theme.image,
    });
    setImageFile(null);
    if (fileInputRef.current) fileInputRef.current.value = null;
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setCurrentTheme({ id: null, name: "", color: "#000000", description: "", image: null });
    setImageFile(null);
    if (fileInputRef.current) fileInputRef.current.value = null;
  };

  // Form change handler
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setCurrentTheme((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // File change handler
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && !["image/jpeg", "image/png", "image/gif"].includes(file.type)) {
      alert("Vui lòng chọn file ảnh (JPG, PNG, GIF)");
      if (fileInputRef.current) fileInputRef.current.value = null;
      setImageFile(null);
      return;
    }
    if (file && file.size > 5 * 1024 * 1024) {
      alert("File ảnh không được vượt quá 5MB");
      if (fileInputRef.current) fileInputRef.current.value = null;
      setImageFile(null);
      return;
    }
    setImageFile(file);
  };

  // Remove image handler
  const handleRemoveImage = () => {
    setImageFile(null);
    if (fileInputRef.current) fileInputRef.current.value = null;
  };

  // Add or update theme
  const handleSaveTheme = async () => {
    if (!currentTheme.name.trim()) {
      alert("Tên theme không được để trống");
      return;
    }
    if (!currentTheme.description.trim()) {
      alert("Mô tả không được để trống");
      return;
    }

    const formData = new FormData();
    formData.append("name", currentTheme.name);
    formData.append("color", currentTheme.color);
    formData.append("description", currentTheme.description);
    if (imageFile) {
      formData.append("image", imageFile);
    } else if (!isEditing) {
      alert("Vui lòng chọn một file ảnh cho theme mới");
      return;
    }

    try {
      if (isEditing) {
        await axios.put(
          `http://localhost:8080/theme/update/${currentTheme.id}`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        alert("Cập nhật theme thành công");
      } else {
        await axios.post("http://localhost:8080/theme/create", formData, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        alert("Thêm theme thành công");
      }
      fetchAllThemes();
      closeModal();
    } catch (error) {
      console.error("Error saving theme:", error);
      alert(
        error.response?.data || "Lỗi khi lưu theme. Vui lòng kiểm tra server."
      );
    }
  };

  // Delete theme
  const handleDeleteTheme = async (id) => {
    if (window.confirm("Bạn có chắc muốn xóa theme này?")) {
      try {
        await axios.delete(`http://localhost:8080/theme/delete/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        alert("Xóa theme thành công");
        fetchAllThemes();
      } catch (error) {
        alert(error.response?.data?.error || "Lỗi khi xóa theme");
      }
    }
  };

  return (
            <div className="admin-content">
    <div className="theme-content">
      <div className="theme-header">
        <h3>Danh sách theme</h3>
      </div>

      {/* Filter */}
      <div className="theme-filter">
        <input
          type="text"
          name="name"
          placeholder="Tìm kiếm theo tên theme"
          value={search.name}
          onChange={handleSearchChange}
        />
        <input
          type="text"
          name="time"
          placeholder="Tìm kiếm theo thời gian"
          value={search.time}
          onChange={handleSearchChange}
        />
        <button className="theme-add-btn" onClick={openAddModal}>
          <i className="fa-solid fa-plus"></i> Thêm Theme
        </button>
      </div>

      {/* Theme Table */}
      <table className="theme-table">
        <thead>
          <tr>
            <th>STT</th>
            <th>Tên Theme</th>
            <th>Màu</th>
            <th>Mô tả</th>
            <th>Hình Nền</th>
            <th>Thời Gian</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          {currentThemes.map((theme, index) => (
            <tr key={theme.id}>
              <td>{indexOfFirstTheme + index + 1}</td>
              <td>{theme.name}</td>
              <td>
                <div
                  style={{
                    backgroundColor: theme.color || "#fff",
                    width: "30px",
                    height: "30px",
                    border: "1px solid #ccc",
                    borderRadius: "4px",
                    display: "inline-block",
                    marginRight: "8px",
                  }}
                ></div>
                {theme.color}
              </td>
              <td>{theme.description || "Không có"}</td>
              <td>
                {theme.image ? (
                  <img
                    src={theme.image}
                    alt={theme.name}
                    style={{
                      width: "50px",
                      height: "50px",
                      objectFit: "cover",
                      borderRadius: "4px",
                    }}
                  />
                ) : (
                  "Không có"
                )}
              </td>
              <td>{formatTime(theme.createdAt)}</td>
              <td>
                <button
                  className="theme-edit-btn"
                  onClick={() => openEditModal(theme)}
                >
                  <i className="fa-solid fa-edit"></i>
                </button>
                <button
                  className="theme-delete-btn"
                  onClick={() => handleDeleteTheme(theme.id)}
                >
                  <i className="fa-solid fa-trash"></i>
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Pagination */}
      <div className="theme-pagination">
        <ul className="pagination-list">
          <li>
            <button
              onClick={() => paginate(currentPage - 1)}
              disabled={currentPage === 1}
              className="pagination-btn"
            >
              <i className="fa-solid fa-arrow-left"></i>
            </button>
          </li>
          {pageNumbers.map((number) => (
            <li key={number}>
              <button
                onClick={() => paginate(number)}
                className={`pagination-btn ${
                  currentPage === number ? "active" : ""
                }`}
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
              <i className="fa-solid fa-arrow-right"></i>
            </button>
          </li>
        </ul>
      </div>

      {/* Modal for Add/Edit Theme */}
      {showModal && (
        <div className="theme-modal">
          <div className="modal-content">
            <h3>{isEditing ? "Chỉnh sửa Theme" : "Thêm Theme Mới"}</h3>
            <div className="form-group">
              <label>Tên Theme</label>
              <input
                type="text"
                name="name"
                value={currentTheme.name}
                onChange={handleFormChange}
                placeholder="Nhập tên theme"
              />
            </div>
            <div className="form-group">
              <label>Mô tả</label>
              <textarea
                name="description"
                value={currentTheme.description}
                onChange={handleFormChange}
                placeholder="Nhập mô tả theme"
                className="description-input"
              />
            </div>
            <div className="form-group">
              <label>Chọn Màu</label>
              <div className="color-picker-container">
                <input
                  type="color"
                  name="color"
                  value={currentTheme.color}
                  onChange={handleFormChange}
                  className="color-picker"
                />
                <span className="color-code">{currentTheme.color}</span>
              </div>
            </div>
            <div className="form-group">
              <label>Chọn Hình Ảnh</label>
              <input
                type="file"
                ref={fileInputRef}
                onChange={handleFileChange}
                accept="image/jpeg,image/png,image/gif"
                className="file-input"
              />
              <div className="image-preview-container">
                {isEditing && currentTheme.image && (
                  <div className="image-preview">
                    <h4>Hình ảnh hiện tại</h4>
                    <div className="preview-container">
                      <img
                        src={currentTheme.image}
                        alt="Current Theme Image"
                        className="preview-image"
                      />
                    </div>
                  </div>
                )}
                {imageFile && (
                  <div className="image-preview">
                    <h4>Hình ảnh mới</h4>
                    <div className="preview-container">
                      <img
                        src={URL.createObjectURL(imageFile)}
                        alt="New Image Preview"
                        className="preview-image"
                      />
                      <button
                        className="remove-image-btn"
                        onClick={handleRemoveImage}
                      >
                        <i className="fa-solid fa-times"></i> Xóa
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </div>
            <div className="modal-buttons">
              <button className="save-btn" onClick={handleSaveTheme}>
                Lưu
              </button>
              <button className="cancel-btn" onClick={closeModal}>
                Hủy
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
        </div>
  );
};

export default AdminThemeContent;