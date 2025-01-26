import axios from "axios";
import React, { useEffect, useState } from "react";
import { UserProfileFuntions } from "../../../auth/UserProfile";
import debounce from "lodash.debounce";
const UpdateProfileComponent = () => {
    const { userData } = UserProfileFuntions();
    const [profileData, setProfileData] = useState({
        firstName: "",
        lastName: "",
        nickName: "",
        birthday: "",
        phone: "",
        description: "",
        gender: "",
        relationship: "",
        school: "",
        university: "",
        address: "",
        city: "",
        state: "",
        country: "",
        zip: "",
    });

    // Đồng bộ hóa profileData với userData
    useEffect(() => {
        if (userData) {
            setProfileData((prevData) => ({
                ...prevData,
                firstName: userData.firstName || "",
                lastName: userData.lastName || "",
                nickName: userData.nickName || "",
                birthday: userData?.birthday ? formatDate(userData?.birthday) : "",
                phone: userData.phone || "",
                description: userData.description || "",
                gender: userData.gender || "",
                relationship: userData.relationship || "",
                school: userData.school || "",
                university: userData.university || "",
                address: userData.address || "",
                city: userData.city || "",
                state: userData.state || "",
                country: userData.country || "",
                zip: userData.zip || "",
            }));
        }
    }, [userData]); // Chỉ chạy khi `userData` thay đổi

    // Gửi yêu cầu cập nhật
    const updateProfile = async () => {
        try {
          const response =  await axios.put(
                "http://localhost:8080/user/update",
                profileData,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`, // Thêm token vào header
                    },
                }
            );
            console.log("Profile updated successfully:", profileData);
            alert("Cập nhật thành công!");
        } catch (error) {
            console.error("Error updating profile:", error);
            alert(error.response.data);
        }
    };

    // Xử lý khi thay đổi input
    const handleChange = (e) => {
        const { name, value } = e.target;
        setProfileData((prevData) => ({
            ...prevData,
            [name]: value, // Cập nhật giá trị dựa trên name của input
        }));
    };
    const formatDate = (timestamp) => {
        if (!timestamp) return ""; // Nếu timestamp rỗng, trả về chuỗi rỗng
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0"); // Tháng bắt đầu từ 0
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
    };
    return (
        <>
            <div className="setting-content-group">
                <div className="setting-content-group-input">
                    <label className="label-content">Họ</label>
                    <br />
                    <input
                        type="text"
                        placeholder="Nhập họ của bạn"
                        className="setting-content-input"
                        name="firstName"
                        value={profileData.firstName}
                        onChange={handleChange}
                    />
                </div>
            </div>
            <div className="setting-content-group">
                <div className="setting-content-group-input">
                    <label className="label-content">Tên</label>
                    <br />
                    <input
                        type="text"
                        placeholder="Nhập tên của bạn"
                        className="setting-content-input"
                        name="lastName"
                        value={profileData.lastName}
                        onChange={handleChange}
                    />
                </div>
            </div>
            <div className="setting-content-group">
                <div className="setting-content-group-input">
                    <label className="label-content">Biệt danh</label>
                    <br />
                    <input
                        type="text"
                        placeholder="Nhập biệt danh của bạn"
                        className="setting-content-input"
                        name="nickName"
                        value={profileData.nickName}
                        onChange={handleChange}
                    />
                </div>
            </div>
            <div className="setting-content-group">
                <div className="setting-content-group-input">
                    <label className="label-content">Ngày sinh</label>
                    <br />
                    <input
                        type="date"
                        className="setting-content-input"
                        name="birthday"
                        value={profileData.birthday}
                        onChange={handleChange}
                    />
                </div>
            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Số điện thoại</label>
                    <br />
                    <input
                        type='text'
                        name="phone"
                        placeholder='Nhập trạng thái của bạn'
                        className='setting-content-input'
                        value={profileData.phone}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Mô tả</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập miêu tả về bạn'
                        className='setting-content-input'
                           name="description"
                        value={profileData.description}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Giới tính</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập giới tính của bạn'
                        className='setting-content-input'
                           name="gender"
                        value={profileData.gender}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Trạng thái</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập trạng thái của bạn'
                        className='setting-content-input'
                           name="relationship"
                        value={profileData.relationship}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Trường trung học</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập trường trung học của bạn'
                        className='setting-content-input'
                           name="school"
                        value={profileData.school}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Trường đại học</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập trường đại học của bạn'
                        className='setting-content-input'
                           name="university"
                        value={profileData.university}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Địa chỉ</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập Địa chỉ của bạn'
                        className='setting-content-input'
                           name="address"
                        value={profileData.address}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Tỉnh/Thành phố</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập Tỉnh thành '
                        className='setting-content-input'
                        value={profileData.city}
                        onChange={handleChange}
                           name="city"
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Quận/Huyện</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập Quận/Huyện'
                        className='setting-content-input'
                        value={profileData.state}
                           name="state"
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Quốc gia</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập Quốc gia của bạn'
                        className='setting-content-input'
                           name="country"
                        value={profileData.country}
                        onChange={handleChange}
                    />
                </div>

            </div>
            <div className='setting-content-group'>
                <div className='setting-content-group-input'>
                    <label className='label-content'>Mã bưu chính</label>
                    <br />
                    <input
                        type='text'
                        placeholder='Nhập mã bưu chính'
                        className='setting-content-input'
                           name="zip"
                        value={profileData.zip}
                        onChange={handleChange}
                    />
                </div>
            </div>

            <button onClick={() => updateProfile()} className="save-profle-button">Lưu</button>
        </>
    );
};

export default UpdateProfileComponent;
