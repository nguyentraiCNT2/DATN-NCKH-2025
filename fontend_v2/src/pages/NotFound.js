import React from "react";
import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div style={styles.container}>
      <div style={styles.content}>
        <h1 style={styles.title}>404</h1>
        <p style={styles.message}>Trang bạn đang tìm kiếm không tồn tại.</p>
        <Link to="/" style={styles.button}>
          Quay lại trang chủ
        </Link>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    backgroundColor: "#f0f2f5",
    fontFamily: "Arial, sans-serif",
  },
  content: {
    textAlign: "center",
  },
  title: {
    fontSize: "120px",
    fontWeight: "bold",
    color: "#1877f2",
    margin: "0",
  },
  message: {
    fontSize: "24px",
    color: "#606770",
    margin: "10px 0",
  },
  button: {
    display: "inline-block",
    marginTop: "20px",
    padding: "10px 20px",
    backgroundColor: "#1877f2",
    color: "#fff",
    textDecoration: "none",
    borderRadius: "5px",
    fontSize: "16px",
  },
};

export default NotFound;