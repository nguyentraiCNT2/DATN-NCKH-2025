import React, { useState } from "react";
import axios from "axios";
const SecurityProfile = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const changePassword = async () => {
        if (!oldPassword || !newPassword || !confirmPassword) {
            alert("Vui lòng điền đầy đủ thông tin!");
            return;
        }

        if (newPassword !== confirmPassword) {
            alert("Mật khẩu mới và xác nhận mật khẩu không khớp!");
            return;
        }

        try {
            const response = await axios.put(
                `http://localhost:8080/user/changepassword`, 
                null, // Body là null vì bạn sử dụng @RequestParam
                {
                    params: {
                        oldPassword,
                        newPassword,
                        confirmPassword,
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`, // Thêm token xác thực nếu cần
                    },
                }
            );
            alert(response.data); // Thông báo thành công

            setNewPassword('')
            setConfirmPassword('')
            setOldPassword('')
        } catch (error) {
            console.error("Error changing password:", error);
            alert(
                error.response?.data || "Đã xảy ra lỗi, vui lòng thử lại!"
            );
        }
    };
    return (
        <>
            <div className="setting-content-group">
                <div className="setting-content-group-input">
                    <label className="label-content">Mật khẩu hiện tại</label>
                    <br />
                    <input
                            type="password"
                        placeholder="Nhập mật khẩu hiện tại của bạn"
                        className="setting-content-input"
                        name="oldPassword"
                        value={oldPassword}
                        onChange={(e) => setOldPassword(e.target.value)}
                    />
                </div>
            </div>
            <div className="setting-content-group">

                <div className="setting-content-group-input">
                    <label className="label-content">Mật khẩu mới</label>
                    <br />
                    <input
                          type="password"
                        placeholder="Nhập mật khẩu mới của bạn"
                        className="setting-content-input"
                        name="newPassword"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
                </div>
            </div>
            <div className="setting-content-group">
                <div className="setting-content-group-input">
                    <label className="label-content">Xác nhận Mật khẩu </label>
                    <br />
                    <input
                        type="password"
                        placeholder="Nhập lại mật khẩu của bạn"
                        className="setting-content-input"
                        name="confirmPassword"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                </div>
            </div>
           
            <button    onClick={changePassword} className="save-profle-button">Lưu</button>
        </>
    );
};

export default SecurityProfile;
