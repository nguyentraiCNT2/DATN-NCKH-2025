import axios from 'axios';
import React, { useState } from 'react';
import '../assets/css/login.css';
const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/auth/login',
                new URLSearchParams({
                    username,
                    password
                }),
                {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    withCredentials: true
                }
            );

            if (response.status === 200) {
                const token = response.data.token;
                localStorage.setItem('token', token);
                localStorage.setItem('userId', response.data.userId);
                localStorage.setItem('role', response.data.role);
                console.log('Logged in successfully', token);

                window.location.href = '/';

            }
        } catch (error) {
            setErrorMessage(error.response.data.error);
            console.error('Login error:', error);
        }
    };

    return (
        <div>

            <div class="login-container">
                <div class="login-bg">
                    <img src="/img/NTU.png" alt="" class="login-bg-img" />
                </div>
                <div class="login-from-group">
                    <div class="logo-title">
                        <img src="/img/snapedit_1726661445269.png" alt="" width="50px" height="50px" />
                        <h2 class="title">Đăng nhập</h2>
                    </div>
                    <div class="login-form" >
                        <form action="" method="post" class="form-login" onSubmit={handleLogin}>
                            <div class="login-form-input">
                                <input type="text" placeholder="E-mail của bạn" class="input-control" value={username} onChange={(e) => setUsername(e.target.value)} />
                                <input type="password" placeholder="Mật khẩu " class="input-control" value={password} onChange={(e) => setPassword(e.target.value)} />
                                <input type="submit" value="Đăng nhập" class="submit-control" />
                                <div class="fogotpassword">
                                    <a href="#" class="fogotpassword-action">Quên mật khẩu</a>
                                </div>
                                <hr />
                                <div class="title-register">
                                    <p class="title-p">Bạn chưa có tài khoản đăng ký <a href="/register" class="register-action"> tại đây</a></p>
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
                            <label for="email">E-mail</label>
                            <input type="text" placeholder="E-mail của bạn" class="mobile-input-control" value={username} onChange={(e) => setUsername(e.target.value)} />
                            <label for="password">Mật khẩu  </label>
                            <input type="password" placeholder="Mật khẩu " class="mobile-input-control" value={password} onChange={(e) => setPassword(e.target.value)} />
                            <input type="submit" value="Đăng nhập" class="mobile-submit-control" />
                            <div class="mobile-fogotpassword">
                                <a href="#" class="mobile-fogotpassword-action">Bạn quên mật khẩu?</a>
                            </div>
                            <hr />
                            <div class="mobile-title-register">
                                <a href="/register" class="mobile-register-action">Đăng ký</a>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;