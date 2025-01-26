import { Route, BrowserRouter as Router, Routes, useLocation } from 'react-router-dom';
import PrivateRoute from './auth/PrivateRoute';
import './App.css';
import './assets/css/postCard.css'
import Header from './components/Header/Header';
import ChatPage from './pages/ChatPage';
import FriendProfilePage from './pages/FriendProfile';
import HomePage from './pages/HomePage';
import ListGroupPage from './pages/ListGroupPage';
import ListFriendPage from './pages/ListUserPage';
import LoginPage from './pages/LoginPage';
import ProfilePage from './pages/ProfilePage';
import RegisterPage from './pages/RegisterPage';
import PostDetailPage from './pages/PostDetailPage';
import ListFollowPage from './pages/Friend/ListFollowPage';
import ListFollowerPage from './pages/Friend/ListFollowerPage';
import FollowPage from './pages/FollowPage';
import SettingPage from './pages/Setting/SettingPage';
import NotificationPage from './pages/Notification/NotificationPage';
import GroupProfilePage from './pages/GroupProfile';
const Layout = ({ children }) => {
  const location = useLocation();

  return (
    <>
      {location.pathname !== '/login' && location.pathname !== '/register' && location.pathname !== '/active/email'
      && location.pathname !== '/admin' &&  !location.pathname.startsWith('/admin') && (
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
        <Route path="/list-follow" element={<ListFollowPage />} />
        <Route path="/list-follower" element={<ListFollowerPage />} />
        <Route path="/message" element={<ChatPage />} />
        <Route path="/setting" element={<SettingPage />} />
        <Route path="/notification" element={<NotificationPage />} />
     </Route>
       
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        </Routes>
      </Layout>
    </Router>
    </div>
  );
}

export default App;
