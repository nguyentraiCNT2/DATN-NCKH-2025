import axios from 'axios';
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import '../assets/css/login.css';
const ResetPasswordPage = () => {
    const {email} = useParams();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/auth/resetpassword',
                new URLSearchParams({
                    email: email,
                    newPassword: newPassword,
                    confirmPassword: confirmPassword,
                }),
                {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    withCredentials: true
                }
            );

            if (response.status === 200) {
               alert('Thay đổi mật khẩu thành công! ');
               window.location.href = '/login';
            }
        } catch (error) {
            alert(error.response.data.error)
            console.error('Login error:', error);
        }
    };

    return (
        <div>

            <div class="login-container">
                <div class="login-bg">
                    <img src="https://naict.tttt.nghean.gov.vn/uploads/news/2024/02/anhbaimangxh.jpg" alt="" class="login-bg-img" />
                </div>
                <div class="login-from-group">
                    <div class="logo-title">
                        <img src="/img/snapedit_1726661445269.png" alt="" width="50px" height="50px" />
                        <h2 class="title">Thay đổi mật khẩu</h2>
                    </div>
                    <div class="login-form" >
                        <form action="" method="post" class="form-login" onSubmit={handleLogin}>
                            <div class="login-form-input">
                                <input type="password" placeholder="Mật khẩu mới " class="input-control" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
                                <input type="password" placeholder="Xác nhận mật khẩu " class="input-control" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
                             
                                <input type="submit" value="Lưu" class="submit-control" />
                                <hr />
                                <div class="title-register">
                                    <p class="title-p">Quay lại đăng nhập <a href="/login" class="register-action"> tại đây</a></p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="mobile-login-container">
                <div class="mobile-logo-title">
                    <img src="img/snapedit_1726661445269.png" alt="" class="mobile-logo" width="100px" height="100px" />
                </div>
                <div class="mobile-login-form">
                    <form action="" method="post" class="mobile-form-login" onSubmit={handleLogin}>
                        <div class="mobile-login-form-input">
                            <label for="email">Mật khẩu mới</label>
                            <input type="password" placeholder="Nhập mật khẩu mới" class="mobile-input-control" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
                            <label for="password">nhập lại mật khẩu  </label>
                            <input type="password" placeholder="Nhập lại mmật khẩu " class="mobile-input-control" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
                            <input type="submit" value="Lưu" class="mobile-submit-control" />
                            <hr />
                            <div class="mobile-title-register">
                                <a href="/login" class="mobile-register-action">Đăng ký</a>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default ResetPasswordPage;