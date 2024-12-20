package DataBaseSQL;

import java.sql.*;
import java.time.LocalDate;

public class BorrowedBookDataBase {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    public void borrowBook(String userName, String bookName) throws SQLException {
        String query = "INSERT INTO borrowedBooks(userName, bookName, borrowDate, returnDate) " +
                "VALUES (?, ?, CURRENT_DATE, ?);";
        try (Connection con = databaseConnection.getDBConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, userName);  // Liên kết với UserDataBase
                preparedStatement.setString(2, bookName);

                //set return date = 7 days after the day the book was borrowed
                LocalDate returnDate = LocalDate.now(). plusWeeks(1);
                preparedStatement.setDate(3, Date.valueOf(returnDate));

                preparedStatement.executeUpdate();
                System.out.println("Book " + bookName + " borrowed by " + userName);
            } catch (SQLException e) {
                System.err.println("Error borrowing book: " + e.getMessage());
                throw e;
            }
        }
    }

    public void removeBookFromBorrowed(String userName, String bookName) throws SQLException {
        String query = "DELETE FROM borrowedBooks WHERE userName = ? AND bookName = ?";

        try (Connection con = databaseConnection.getDBConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, userName); // Set tên người dùng
                preparedStatement.setString(2, bookName); // Set tên sách

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Sách '" + bookName + "' đã được xóa khỏi danh sách mượn của người dùng " + userName);
                } else {
                    System.out.println("Không tìm thấy sách '" + bookName + "' trong danh sách mượn của người dùng " + userName);
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi xóa sách khỏi danh sách mượn.");
                e.printStackTrace();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Không thể kết nối với cơ sở dữ liệu.");
            e.printStackTrace();
            throw e;
        }
    }

//    public void returnBook(String userName, String bookName) throws SQLException {
//        String checkQuery = "SELECT returnDate FROM borrowedBooks WHERE userName = ? AND bookName = ?";
//        String updateQuery = "UPDATE borrowedBooks SET returnDate = CURRENT_DATE WHERE userName = ? AND bookName = ?";
//        String deleteQuery = "DELETE FROM borrowedBooks WHERE userName = ? AND bookName = ?";
//
//        try (Connection con = databaseConnection.getDBConnection()) {
//            // Kiểm tra xem sách đã được mượn chưa và nếu đã đến hạn trả hay chưa
//            try (PreparedStatement checkStatement = con.prepareStatement(checkQuery)) {
//                checkStatement.setString(1, userName);
//                checkStatement.setString(2, bookName);
//
//                ResultSet resultSet = checkStatement.executeQuery();
//                if (resultSet.next()) {
//                    Date returnDate = resultSet.getDate("returnDate");
//
//                    // Nếu sách đã đến hạn hoặc đã trả, tiến hành cập nhật và xóa
//                    if (returnDate == null || returnDate.compareTo(new Date()) <= 0) {
//                        // Cập nhật ngày trả sách nếu chưa trả
//                        try (PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {
//                            updateStatement.setString(1, userName);
//                            updateStatement.setString(2, bookName);
//                            updateStatement.executeUpdate();
//                            System.out.println("Sách đã được trả, ngày trả là: " + new Date());
//                        }
//
//                        // Xóa sách khỏi borrowedBooks sau khi trả
//                        try (PreparedStatement deleteStatement = con.prepareStatement(deleteQuery)) {
//                            deleteStatement.setString(1, userName);
//                            deleteStatement.setString(2, bookName);
//                            deleteStatement.executeUpdate();
//                            System.out.println("Sách '" + bookName + "' đã được xóa khỏi danh sách mượn của người dùng " + userName);
//                        }
//                    } else {
//                        System.out.println("Sách vẫn chưa đến hạn trả.");
//                    }
//                } else {
//                    System.out.println("Không tìm thấy sách mượn của người dùng " + userName);
//                }
//            } catch (SQLException e) {
//                System.err.println("Lỗi khi kiểm tra sách.");
//                e.printStackTrace();
//                throw e;
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi kết nối cơ sở dữ liệu.");
//            e.printStackTrace();
//            throw e;
//        }
//    }
}

