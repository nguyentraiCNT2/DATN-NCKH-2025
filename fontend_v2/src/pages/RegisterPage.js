import axios from 'axios';
import React, { useState } from 'react';
import '../assets/css/login.css';
const RegisterPage = () => {
    const [fullName, setFullname] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');
    const [otp, setOtp] = useState('');
    const [emailVerified, setEmailVerified] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [success, setSuccess] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(
                'http://localhost:8080/auth/register',
                {
                    email,
                    fullName,
                    phone,
                    password,
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                setEmailVerified(true);
                setSuccess('Đăng ký thành công, vui lòng kiểm tra email để kích hoạt.');
                setErrorMessage('');
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.error || 'Đăng ký thất bại');
            console.error('Registration error:', error);
        }
    };

    const handleActivateEmail = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(
                'http://localhost:8080/auth/active/email',
                {
                    email,
                    otp,
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                const token = response.data.token;
                localStorage.setItem('token', token); // Lưu token vào localStorage
                setSuccess('Email đã được kích hoạt thành công.');
                setErrorMessage('');
                window.location.href = '/login'; // Chuyển hướng tới trang login
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.error || 'Kích hoạt email thất bại');
            setSuccess('');
            console.error('Activation error:', error);
        }
    };

    const handleResendEmail = async (e) => {

        try {
            const response = await axios.post(
                `http://localhost:8080/auth/resendEmail?email=${email}`,
                null,
                {
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                setSuccess('Mã xác thực đã được gửi lại đến email của bạn.');
                setErrorMessage('');
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.error || 'Gửi lại mã xác thực thất bại');
            setSuccess('');
            console.error('Resend activation email error:', error);
        }
    };

    return (
        <div>
            <div className="login-container">
                <div className="login-bg">
                    <img src="https://naict.tttt.nghean.gov.vn/uploads/news/2024/02/anhbaimangxh.jpg" alt="" className="login-bg-img" />
                </div>
                <div className="login-from-group">
                    <div className="logo-title">
                        <img src="img/snapedit_1726661445269.png" alt="" width="50px" height="50px" />
                        <h2 className="title">Đăng ký</h2>
                    </div>
                    <div className="login-form">
                        <form className="form-login" onSubmit={handleRegister}>
                            <div className="login-form-input">
                                <input type="text" placeholder="Họ và tên" className="input-control" value={fullName} onChange={(e) => setFullname(e.target.value)} />
                                <input type="text" placeholder="E-mail của bạn" className="input-control" value={email} onChange={(e) => setEmail(e.target.value)} />
                                <input type="text" placeholder="Số điện thoại" className="input-control" value={phone} onChange={(e) => setPhone(e.target.value)} />
                                <input type="password" placeholder="Mật khẩu" className="input-control" value={password} onChange={(e) => setPassword(e.target.value)} />
                                <input type="submit" value="Đăng ký" className="submit-control" />
                                <hr />
                                <div className="title-register">
                                    <p className="title-p">
                                        Bạn đã có tài khoản đăng nhập{' '}
                                        <a href="/login" className="register-action">tại đây</a>
                                    </p>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            {emailVerified && (
                <div class="overlay">
                    <form class="verification-form" onSubmit={handleActivateEmail}>
                        <div class="form-title">Nhập mã xác thực</div>
                        <div class="verification-input-group">
                            <input
                                type="text"
                                class="verification-input"
                                placeholder="Nhập mã 6 số"
                                value={otp}
                                onChange={(e) => setOtp(e.target.value)} />
                            <a class="resend-button" onClick={() => handleResendEmail()}>Gửi lại </a>
                        </div>
                        <input type='submit' class="verification-button" value="Xác nhận" />
                    </form>
                </div>
            )}


            <div className="mobile-login-container">
                <div className="mobile-logo-title">
                    <img
                        src="img/snapedit_1726661445269.png"
                        alt=""
                        className="mobile-logo"
                        width="100px"
                        height="100px"
                    />
                </div>
                <div className="mobile-login-form">
                    <form  className="mobile-form-login" onSubmit={handleRegister}>
                        <div className="mobile-login-form-input">
                            <label htmlFor="fullname">Họ và tên</label>
                            <input
                                type="text"
                                placeholder="Nhập họ tên của bạn"
                                className="mobile-input-control"
                                value={fullName} 
                                onChange={(e) => setFullname(e.target.value)}
                            />
                            <label htmlFor="email">E-mail</label>
                            <input
                                type="text"
                                placeholder="E-mail của bạn"
                                className="mobile-input-control"
                                value={email} 
                                onChange={(e) => setEmail(e.target.value)}
                            />
                            <label htmlFor="phone">Số điện thoại</label>
                            <input
                                type="text"
                                placeholder="Nhập số điện thoại của bạn"
                                className="mobile-input-control"
                                value={phone}
                                 onChange={(e) => setPhone(e.target.value)}
                            />
                            <label htmlFor="password">Mật khẩu</label>
                            <input
                                type="password"
                                placeholder="Mật khẩu"
                                className="mobile-input-control"
                                value={password}
                                 onChange={(e) => setPassword(e.target.value)}
                            />
                            <input type="submit" value="Đăng ký" className="mobile-submit-control" />
                            <hr />
                            <div className="mobile-title-register">
                                <a href="/login" className="mobile-register-action">Đăng nhập</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
