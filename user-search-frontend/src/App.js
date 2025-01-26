import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Row, Col, Form, Button, Table, Dropdown, DropdownButton } from 'react-bootstrap';
import { FaSearch } from 'react-icons/fa';

const API_URL = 'http://localhost:8080/api/users/search';

function App() {
  const [query, setQuery] = useState('');
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedRole, setSelectedRole] = useState('');
  const [sortConfig, setSortConfig] = useState({ key: 'name', direction: 'asc' });

  const handleSearchInputChange = (e) => {
    setQuery(e.target.value);
  };

  const handleSearch = async () => {
    if (query.length >= 0) {
      setLoading(true);
      try {
        const response = await axios.get(API_URL, { params: { query } });
        setUsers(response.data);
      } catch (error) {
        console.error('Error fetching users:', error);
      }
      setLoading(false);
    }
  };

  const handleRoleFilter = (role) => {
    setSelectedRole(role);
  };

  const handleSort = (key) => {
    let direction = 'asc';
    if (sortConfig.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  const filteredAndSortedUsers = users
    .filter(user => (selectedRole ? user.role === selectedRole : true))
    .sort((a, b) => {
      if (sortConfig.key) {
        const aValue = a[sortConfig.key] || '';
        const bValue = b[sortConfig.key] || '';
        if (sortConfig.direction === 'asc') {
          return aValue > bValue ? 1 : -1;
        } else {
          return aValue < bValue ? 1 : -1;
        }
      }
      return 0;
    });

  return (
    <Container className="mt-5" style={{ backgroundColor: '#F4F7FC', padding: '30px', borderRadius: '12px', boxShadow: '0px 10px 20px rgba(0, 0, 0, 0.1)' }}>
      <Row className="justify-content-center">
        <Col className="text-center mb-4">
          <h1
            style={{
              fontSize: '2.5rem',
              fontWeight: '700',
              color: '#3D4B7B',
              textShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
              letterSpacing: '0.5px',
            }}
          >
            User Search
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md={6}>
          <Form.Control
            type="text"
            value={query}
            onChange={handleSearchInputChange}
            placeholder="Enter name or SSN..."
            onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
            className="mb-3"
            style={{
              borderRadius: '30px',
              padding: '12px 20px',
              border: '1px solid #E1E6FA',
              backgroundColor: '#FFFFFF',
              fontSize: '1rem',
              color: '#3D4B7B',
            }}
          />
          <Button
            onClick={handleSearch}
            variant="primary"
            className="w-100"
            style={{
              borderRadius: '30px',
              fontSize: '1.1rem',
              backgroundColor: '#2F8DFF',
              borderColor: '#2F8DFF',
              boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            }}
          >
            <FaSearch style={{ marginRight: '8px' }} /> Search
          </Button>
        </Col>
      </Row>

      <Row className="mt-4">
        <Col>
          <DropdownButton
            variant="secondary"
            title={`Filter by Role: ${selectedRole || 'All'}`}
            onSelect={handleRoleFilter}
            className="d-inline-block me-3"
            style={{ borderRadius: '30px' }}
          >
            <Dropdown.Item eventKey="">All</Dropdown.Item>
            <Dropdown.Item eventKey="admin">Admin</Dropdown.Item>
            <Dropdown.Item eventKey="user">User</Dropdown.Item>
            <Dropdown.Item eventKey="moderator">Moderator</Dropdown.Item>
          </DropdownButton>
        </Col>
      </Row>

      {loading ? (
        <Row className="mt-5">
          <Col className="text-center">
            <h4>Loading...</h4>
          </Col>
        </Row>
      ) : (
        <Row className="mt-5">
          <Col>
            <Table striped bordered hover responsive className="text-center" style={{ backgroundColor: '#FFFFFF', borderRadius: '12px', overflow: 'hidden' }}>
              <thead style={{ backgroundColor: '#2F8DFF', color: '#FFFFFF' }}>
                <tr>
                  <th onClick={() => handleSort('firstName')} style={{ cursor: 'pointer' }}>
                    Name {sortConfig.key === 'firstName' ? (sortConfig.direction === 'asc' ? '▲' : '▼') : ''}
                  </th>
                  <th onClick={() => handleSort('age')} style={{ cursor: 'pointer' }}>
                    Age {sortConfig.key === 'age' ? (sortConfig.direction === 'asc' ? '▲' : '▼') : ''}
                  </th>
                  <th onClick={() => handleSort('role')} style={{ cursor: 'pointer' }}>
                    Role {sortConfig.key === 'role' ? (sortConfig.direction === 'asc' ? '▲' : '▼') : ''}
                  </th>
                  <th onClick={() => handleSort('email')} style={{ cursor: 'pointer' }}>
                    Email {sortConfig.key === 'email' ? (sortConfig.direction === 'asc' ? '▲' : '▼') : ''}
                  </th>
                  <th onClick={() => handleSort('ssn')} style={{ cursor: 'pointer' }}>
                    SSN {sortConfig.key === 'ssn' ? (sortConfig.direction === 'asc' ? '▲' : '▼') : ''}
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredAndSortedUsers.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="text-center">No users found.</td>
                  </tr>
                ) : (
                  filteredAndSortedUsers.map((user) => (
                    <tr key={user.id} style={{ cursor: 'pointer' }} onMouseOver={(e) => e.target.style.backgroundColor = '#f4f7ff'} onMouseOut={(e) => e.target.style.backgroundColor = ''}>
                      <td>{`${user.firstName} ${user.lastName}`}</td>
                      <td>{user.age}</td>
                      <td>{user.role}</td>
                      <td>{user.email}</td>
                      <td>{user.ssn}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          </Col>
        </Row>
      )}
    </Container>
  );
}

export default App;
