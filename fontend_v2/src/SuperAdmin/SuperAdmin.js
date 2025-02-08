import React from 'react';
import SupperAdminHeader from './SupperAdminHeader';
import SupperAdminSidebar from './SupperAdminSidebar';
import SupperAdminUserTable from './SupperAdminUserTable';
import './SuperAdmin.css';

const SuperAdmin = () => {
  return (
    <div className="super-admin-container">
      <SupperAdminHeader />
      <div className="super-admin-content">
        <SupperAdminSidebar />
        <SupperAdminUserTable />
      </div>
    </div>
  );
};

export default SuperAdmin;