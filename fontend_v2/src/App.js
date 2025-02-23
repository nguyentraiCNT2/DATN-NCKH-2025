import { Route, BrowserRouter as Router, Routes, useLocation } from 'react-router-dom';
import './assets/css/postCard.css';
import PrivateRoute from './auth/PrivateRoute';

import AdminCommentList from './Admin/AdminCommentList';
import AdminGroupList from './Admin/AdminGroupList';
import AdminLikeList from './Admin/AdminLikeList';
import AdminListPostByComment from './Admin/AdminListPostByComment';
import AdminLikeListByPost from './Admin/AdminListUserLike';
import AdminPostList from './Admin/AdminPostList';
import AdminReportList from './Admin/AdminReportList';
import Dashboard from './Admin/Dashboard';
import SuperAdmin from './SuperAdmin/SuperAdmin';
import AdminRoute from './auth/AdminRoute';
import SupperAdminRoute from './auth/SupperAdminRoute';
import Header from './components/Header/Header';
import ChatPage from './pages/ChatPage';
import FollowPage from './pages/FollowPage';
import Forbidden from './pages/Forbidden';
import ForgotPasswordPage from './pages/ForgotPassword';
import ListFollowPage from './pages/Friend/ListFollowPage';
import ListFollowerPage from './pages/Friend/ListFollowerPage';
import FriendProfilePage from './pages/FriendProfile';
import GroupProfilePage from './pages/GroupProfile';
import HomePage from './pages/HomePage';
import ListGroupPage from './pages/ListGroupPage';
import ListFriendPage from './pages/ListUserPage';
import LoginPage from './pages/LoginPage';
import NotFound from './pages/NotFound';
import NotificationPage from './pages/Notification/NotificationPage';
import PostDetailPage from './pages/PostDetailPage';
import ProfilePage from './pages/ProfilePage';
import RegisterPage from './pages/RegisterPage';
import ResetPasswordPage from './pages/ResetPasswordPage';
import SearchUserPage from './pages/SearchUserPage';
import SettingPage from './pages/Setting/SettingPage';
const Layout = ({ children }) => {
  const location = useLocation();

  return (
    <>
      {location.pathname !== '/login' && location.pathname !== '/register' && location.pathname !== '/active/email'  && location.pathname !== '/forgotpassword'  && location.pathname !== '/resetpassword'
        && location.pathname !== '/admin' && !location.pathname.startsWith('/admin') && location.pathname !== '/supper/admin' && !location.pathname.startsWith('/supper/admin') && (
          <>
            <Header />

          </>
        )}

      {children}
    </>
  );
};

function App() {
  return (
    <div className="App">
      <Router>
        <Layout>
          <Routes>
            <Route element={<PrivateRoute />}>
              <Route path="/" element={<HomePage />} />
              <Route path="/follow-post" element={<FollowPage />} />
              <Route path="/friend/:id" element={<FriendProfilePage />} />
              <Route path="/post/:id" element={<PostDetailPage />} />
              <Route path="/profile" element={<ProfilePage />} />
              <Route path="/list-group" element={<ListGroupPage />} />
              <Route path="/group/:id" element={<GroupProfilePage />} />
              <Route path="/list-friend" element={<ListFriendPage />} />
           
              <Route path="/list-follower" element={<ListFollowerPage />} />
              <Route path="/message" element={<ChatPage />} />
              <Route path="/setting" element={<SettingPage />} />
              <Route path="/notification" element={<NotificationPage />} />
              <Route path="/search/:keyword" element={<SearchUserPage />} />
            </Route>
            <Route path="/list-follow" element={<ListFollowPage />} />
            <Route element={<AdminRoute />}>
            <Route path="/admin" element={<Dashboard />} />
            <Route path="/admin/post" element={<AdminPostList />} />
            <Route path="/admin/group" element={<AdminGroupList />} />
            <Route path="/admin/report" element={<AdminReportList />} />
            <Route path="/admin/post/comment" element={<AdminListPostByComment />} />
            <Route path="/admin/post/like" element={<AdminLikeList />} />
            <Route path="/admin/post/like/:id" element={<AdminLikeListByPost />} />
            <Route path="/admin/comment" element={<AdminCommentList />} />
            </Route>
            <Route element={<SupperAdminRoute />}>
            <Route path="/supper/admin" element={<SuperAdmin />} />
            </Route>
            
            <Route path="/403" element={<Forbidden />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/forgotpassword" element={<ForgotPasswordPage />} />
            <Route path="/resetpassword/:email" element={<ResetPasswordPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Layout>
      </Router>
    </div>
  );
}

export default App;
