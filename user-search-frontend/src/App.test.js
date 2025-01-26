import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';
import axios from 'axios';

// Mock axios to avoid real API calls during tests
jest.mock('axios');

describe('App Component', () => {

  test('renders the search input and search button', () => {
    render(<App />);

    const searchInput = screen.getByPlaceholderText('Enter name or SSN...');
    const searchButton = screen.getByRole('button', { name: /Search/i });

    expect(searchInput).toBeInTheDocument();
    expect(searchButton).toBeInTheDocument();
  });

  test('loads and displays user data when search button is clicked', async () => {
    const mockResponse = {
      data: [
        {
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
          age: 30,
          role: 'admin',
          email: 'john.doe@example.com',
          ssn: '123-45-6789',
        },
      ],
    };

    axios.get.mockResolvedValue(mockResponse);

    render(<App />);

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    await waitFor(() => {
      const userName = screen.getByText('John Doe');
      expect(userName).toBeInTheDocument();
    });
  });

  test('displays loading message when data is being fetched', async () => {
    axios.get.mockImplementation(() => new Promise(resolve => setTimeout(() => resolve({ data: [] }), 1000)));

    render(<App />);

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    const loadingMessage = screen.getByText(/Loading.../i);
    expect(loadingMessage).toBeInTheDocument();
  });

  test('filters users by role', async () => {
    const mockResponse = {
      data: [
        {
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
          age: 30,
          role: 'admin',
          email: 'john.doe@example.com',
          ssn: '123-45-6789',
        },
        {
          id: 2,
          firstName: 'Jane',
          lastName: 'Doe',
          age: 25,
          role: 'user',
          email: 'jane.doe@example.com',
          ssn: '987-65-4321',
        },
      ],
    };

    axios.get.mockResolvedValue(mockResponse);

    render(<App />);

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    const roleFilterDropdown = screen.getByRole('button', { name: /Filter by Role:/i });
    fireEvent.click(roleFilterDropdown);
    const adminRoleOption = screen.getByRole('menuitem', { name: /Admin/i });
    fireEvent.click(adminRoleOption);

    await waitFor(() => {
      const userName = screen.getByText('John Doe');
      expect(userName).toBeInTheDocument();

      const janeName = screen.queryByText('Jane Doe');
      expect(janeName).not.toBeInTheDocument(); // Ensure Jane Doe is not displayed
    });
  });

  test('sorts users by name', async () => {
    const mockResponse = {
      data: [
        {
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
          age: 30,
          role: 'admin',
          email: 'john.doe@example.com',
          ssn: '123-45-6789',
        },
        {
          id: 2,
          firstName: 'Jane',
          lastName: 'Doe',
          age: 25,
          role: 'user',
          email: 'jane.doe@example.com',
          ssn: '987-65-4321',
        },
      ],
    };

    axios.get.mockResolvedValue(mockResponse);

    render(<App />);

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    const nameHeader = screen.getByText(/Name/i);
    fireEvent.click(nameHeader);

    await waitFor(() => {
      const firstRow = screen.getByText('Jane Doe');
      expect(firstRow).toBeInTheDocument();
    });
  });

  test('displays no users found message when no results match', async () => {
    const mockResponse = { data: [] };
    axios.get.mockResolvedValue(mockResponse);

    render(<App />);

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    await waitFor(() => {
      const noUsersMessage = screen.getByText(/No users found/i);
      expect(noUsersMessage).toBeInTheDocument();
    });
  });

  test('shows filtered users when role is selected', async () => {
    const mockResponse = {
      data: [
        {
          id: 1,
          firstName: 'AdminUser',
          lastName: 'Test',
          age: 40,
          role: 'admin',
          email: 'admin@example.com',
          ssn: '111-22-3333',
        },
        {
          id: 2,
          firstName: 'NormalUser',
          lastName: 'Test',
          age: 30,
          role: 'user',
          email: 'normal@example.com',
          ssn: '444-55-6666',
        },
      ],
    };

    axios.get.mockResolvedValue(mockResponse);

    render(<App />);

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    const roleFilterButton = screen.getByRole('button', { name: /Filter by Role:/i });
    fireEvent.click(roleFilterButton);
    const userRoleOption = screen.getByRole('menuitem', { name: /User/i });
    fireEvent.click(userRoleOption);

    await waitFor(() => {
      const normalUserName = screen.getByText('NormalUser Test');
      expect(normalUserName).toBeInTheDocument();

      const adminUserName = screen.queryByText('AdminUser Test');
      expect(adminUserName).not.toBeInTheDocument();  // Ensure the admin user is not shown
    });
  });
});
