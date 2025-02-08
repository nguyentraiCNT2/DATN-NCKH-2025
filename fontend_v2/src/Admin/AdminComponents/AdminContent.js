import React,{useState, useEffect} from "react";
import "./../styles/AdminContent.css";
import axios from "axios";
function AdminContent() {
  const [totalDashboard, setTotalDashboard] = useState(null);
  const token = localStorage.getItem('token');

  const fetchDashboard = async () => {
    try {
        const url = 'http://localhost:8080/admin/dashboard/total'; // Hiển thị tất cả

        const response = await axios.get(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}` // Thêm token vào header
            }
        });
        setTotalDashboard(response.data);

    } catch (err) {
        console.error('Error fetching user data:', err);
    }
};
  useEffect(() => {
    fetchDashboard();
}, [token]);
  return (
    <div className="admin-content">
      <h3>Overview</h3>
      <div className="admin-stats">
        <div className="admin-stat-card">
          <h4>Total Posts</h4>
          <p>{totalDashboard?.totalPost}</p>
        </div>
        <div className="admin-stat-card">
          <h4>Total Likes</h4>
          <p>{totalDashboard?.totalLike}</p>
        </div>
        <div className="admin-stat-card">
          <h4>Total Groups</h4>
          <p>{totalDashboard?.totalGroup}</p>
        </div>
        <div className="admin-stat-card">
          <h4>Total Reports</h4>
          <p>{totalDashboard?.totalReport}</p>
        </div>
        <div className="admin-stat-card">
          <h4>Total Comments</h4>
          <p>{totalDashboard?.totalComment}</p>
        </div>
      </div>
    </div>
  );
}

export default AdminContent;
