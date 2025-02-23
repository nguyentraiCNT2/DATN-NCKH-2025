import axios from 'axios';
import React, { useState } from 'react';
import '../assets/css/login.css';
const ForgotPasswordPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [otp, setOtp] = useState('');
    const [emailVerified, setEmailVerified] = useState(false);
    const handleForGetPassword = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.get(
                `http://localhost:8080/auth/forgetpassword/${username}`,
                {},
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                setEmailVerified(true);
                alert('Xác thực email thành công, vui lòng kiểm tra email để xác thực mã thay đổi mật khẩu.');
            }
        } catch (error) {
            alert(error.response?.data?.error || 'Đăng ký thất bại');
        }
    };
    const handleActivateEmailForGetPassword = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(
                'http://localhost:8080/auth/active-email-for-get-password',
                {
                    email: username,
                    otp: otp,
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                alert('Email đã được xác thực thành công.');
                window.location.href = `/resetpassword/${username}`; // Chuyển hướng tới trang login
            }
        } catch (error) {
            alert(error.response?.data?.error || 'gửi mã xác thực email thất bại');
            console.error('Activation error:', error);
        }
    };

    const handleResendEmailForgetpassword = async (e) => {
        try {
            const response = await axios.post(
                `http://localhost:8080/auth/password/resendEmail?email=${username}`,
                null,
                {
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                alert('Mã xác thực đã được gửi lại đến email của bạn.');
                setErrorMessage('');
            }
        } catch (error) {
            alert(error.response?.data?.error || 'Gửi lại mã xác thực thất bại');
            console.error('Resend activation email error:', error);
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
                        <h2 class="title">Quên mật khẩu</h2>
                    </div>
                    <div class="login-form" >
                        <p>hãy nhập email để xác thực tài khoản mà bạn quên mật khẩu</p>
                        <br />
                        <form action="" method="post" class="form-login" onSubmit={handleForGetPassword} >
                            <div class="login-form-input">
                                <input type="text" placeholder="E-mail của bạn" class="input-control" value={username} onChange={(e) => setUsername(e.target.value)} />
                                <input type="submit" value="Xác thực" class="submit-control" />
                                <hr />
                                <div class="title-register">
                                    <p class="title-p">Quay lại đăng nhập  <a href="/login" class="register-action"> tại đây</a></p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            {emailVerified && (
                <div class="overlay">
                    <form class="verification-form" onSubmit={handleActivateEmailForGetPassword}>
                        <div class="form-title">Nhập mã xác thực</div>
                        <div class="verification-input-group">
                            <input
                                type="text"
                                class="verification-input"
                                placeholder="Nhập mã 6 số"
                                value={otp}
                                onChange={(e) => setOtp(e.target.value)} />
                            <a class="resend-button" onClick={() => handleResendEmailForgetpassword()}>Gửi lại </a>
                        </div>
                        <input type='submit' class="verification-button" value="Xác nhận" />
                    </form>
                </div>
            )}

            <div class="mobile-login-container">
                <div class="mobile-logo-title">
                    <img src="img/snapedit_1726661445269.png" alt="" class="mobile-logo" width="100px" height="100px" />
                </div>
                <div class="mobile-login-form">
                    <p>hãy nhập email để xác thực tài khoản mà bạn quên mật khẩu</p>
                    <br />
                    <form action="" method="post" class="mobile-form-login" onSubmit={handleForGetPassword}>
                        <div class="mobile-login-form-input">
                            <label for="email">E-mail</label>
                            <input type="text" placeholder="E-mail của bạn" class="mobile-input-control" value={username} onChange={(e) => setUsername(e.target.value)} />
                            <input type="submit" value="Xác thực" class="mobile-submit-control" />

                            <hr />
                            <div class="mobile-title-register">
                                <a href="/Login" class="mobile-register-action">Đăng nhập</a>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default ForgotPasswordPage;